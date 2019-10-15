package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Authority;
import cn.linj2n.melody.domain.Comment;
import cn.linj2n.melody.domain.CommentAuthor;
import cn.linj2n.melody.domain.User;
import cn.linj2n.melody.domain.enumeration.CommentStatus;
import cn.linj2n.melody.domain.enumeration.CommentType;
import cn.linj2n.melody.repository.CommentRepository;
import cn.linj2n.melody.security.AuthoritiesConstants;
import cn.linj2n.melody.security.SecurityUtil;
import cn.linj2n.melody.service.CommentService;
import cn.linj2n.melody.service.UserService;
import cn.linj2n.melody.web.dto.comment.CommentAuthorDTO;
import cn.linj2n.melody.web.dto.comment.CommentDTO;
import cn.linj2n.melody.web.dto.comment.CommentFormDTO;
import cn.linj2n.melody.web.dto.comment.ReplyToCommentDTO;
import cn.linj2n.melody.web.utils.DTOModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private DTOModelMapper dtoModelMapper;

    private UserService userService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, DTOModelMapper dtoModelMapper, UserService userService) {
        this.commentRepository = commentRepository;
        this.dtoModelMapper = dtoModelMapper;
        this.userService = userService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyToThePost(Long postId, CommentFormDTO commentFormDTO) {

        // 1. Fetch current login user information as the author.
        userService.getCurrentLoginUser().map(author -> {

            // 2. Convert commentFormDTO to comment.
            Comment comment = new Comment();
            comment.setContent(commentFormDTO.getContent());
            comment.setCommentType(CommentType.REPLY_TO_POST);
            comment.setCommentStatus(getCommentStatusByAuthorRole(author.getAuthorities()));
            comment.setReplyCount(0L);

            // 3. Saving comment to database.
            commentRepository.save(comment);
            commentRepository.updateCommentDetail(comment.getId(), postId, author.getId(), null, null);

            // TODO: Notify target author by email.
            return author;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyToTheComment(Long postId, Long parentCommentId, CommentFormDTO commentFormDTO) {

        // 1. Fetch current login user information as the author.
        userService.getCurrentLoginUser().map(author -> {

            Long replyToAuthorId = commentFormDTO.getReplyToAuthorId();

            // 2. Convert commentFormDTO to comment.
            Comment comment = new Comment();
            comment.setContent(commentFormDTO.getContent());
            comment.setCommentType(CommentType.REPLY_TO_COMMENT);
            comment.setCommentStatus(getCommentStatusByAuthorRole(author.getAuthorities()));
            comment.setReplyCount(0L);

            // 3. Saving comment to database with parentCommentId and replyToAuthorId.
            commentRepository.save(comment);
            commentRepository.updateCommentDetail(comment.getId(), postId, author.getId(), replyToAuthorId, parentCommentId);

            // TODO: Notify target author by email.
            return comment;
        });
    }

    @Override
    public Page<CommentDTO> listPostComments(Long postId, Pageable pageable) {
        return commentRepository
                .findAllByPostIdAndCommentTypeAndCommentStatus(postId, CommentType.REPLY_TO_POST, CommentStatus.ACTIVE, pageable)
                .map(comment -> {
                    CommentAuthorDTO authorDTO = dtoModelMapper.convertToCommentAuthorDTO(comment.getAuthor());
                    CommentDTO commentDTO = dtoModelMapper.convertToDTO(comment);
                    commentDTO.setAuthor(authorDTO);
                    return commentDTO;
                });
    }

    @Override
    public Page<ReplyToCommentDTO> listRepliesToComment(Long postId, Long parentCommentId, Pageable pageable) {
        return commentRepository
                .findAllByPostIdAndParentCommentIdAndCommentStatus(postId, parentCommentId, CommentStatus.ACTIVE, pageable)
                .map(comment -> {
                    CommentAuthorDTO author = dtoModelMapper.convertToCommentAuthorDTO(comment.getAuthor());
                    CommentAuthorDTO replyToAuthor = dtoModelMapper.convertToCommentAuthorDTO(comment.getReplyToAuthor());
                    ReplyToCommentDTO reply = dtoModelMapper.convertToReplyDTO(comment);
                    reply.setAuthor(author);
                    reply.setReplyToAuthor(replyToAuthor);
                    return reply;
                });
    }

    private CommentStatus getCommentStatusByAuthorRole(Set<Authority> authorities) {
        boolean isAdminOrAuthenticatedRole =
                authorities.stream().anyMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN));
        return isAdminOrAuthenticatedRole
                ? CommentStatus.ACTIVE
                : CommentStatus.BLOCK;
    }
}
