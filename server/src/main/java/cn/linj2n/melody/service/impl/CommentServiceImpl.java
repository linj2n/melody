package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Comment;
import cn.linj2n.melody.domain.CommentAuthor;
import cn.linj2n.melody.domain.enumeration.CommentAuthorRole;
import cn.linj2n.melody.domain.enumeration.CommentStatus;
import cn.linj2n.melody.domain.enumeration.CommentType;
import cn.linj2n.melody.repository.CommentAuthorRepository;
import cn.linj2n.melody.repository.CommentRepository;
import cn.linj2n.melody.security.SecurityUtil;
import cn.linj2n.melody.service.CommentService;
import cn.linj2n.melody.web.dto.comment.CommentDTO;
import cn.linj2n.melody.web.dto.comment.CommentFormDTO;
import cn.linj2n.melody.web.dto.comment.ReplyToCommentDTO;
import cn.linj2n.melody.web.utils.DTOModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;


@Service
public class CommentServiceImpl implements CommentService {

    private static final int AUTHOR_NAME_DEFAULT_LENGTH = 8;

    private static final String ADMIN_EMAIL = "linj2n@163.com";

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private CommentAuthorRepository commentAuthorRepository;

    private CommentRepository commentRepository;

    private DTOModelMapper dtoModelMapper;


    public CommentServiceImpl(CommentAuthorRepository commentAuthorRepository, CommentRepository commentRepository, DTOModelMapper dtoModelMapper) {
        this.commentAuthorRepository = commentAuthorRepository;
        this.commentRepository = commentRepository;
        this.dtoModelMapper = dtoModelMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyToThePost(Long postId, CommentFormDTO commentFormDTO) {

        if (commentFormDTO.getEmail().equals(ADMIN_EMAIL) && !SecurityUtil.isCurrentUserInRole(ROLE_ADMIN)) {
            // TODO: Not good design, need to refactor.
            return ;
        }

        CommentAuthor author = commentAuthorRepository
                .findOptionalByEmail(commentFormDTO.getEmail())
                .orElseGet(() -> createNewAuthorByEmail(commentFormDTO.getEmail()));

        Comment comment = new Comment();
        comment.setContent(commentFormDTO.getContent());
        comment.setCommentType(CommentType.REPLY_TO_POST);
        comment.setCommentStatus(getCommentStatusByAuthorRole(author.getRole()));
        comment.setReplyCount(0L);

        commentRepository.save(comment);
        commentRepository.updateCommentDetail(comment.getId(), postId, author.getId(), null, null);

        // TODO: Notify target author by email.
    }

    private CommentAuthor createNewAuthorByEmail(String email) {
        CommentAuthor newAuthor = new CommentAuthor();
        newAuthor.setEmail(email);
        newAuthor.setId(null);
        newAuthor.setRole(CommentAuthorRole.VISITOR);
        String emailHashCode = DigestUtils.md5DigestAsHex(newAuthor.getEmail().getBytes());
        newAuthor.setMd5Code(emailHashCode);
        newAuthor.setName(emailHashCode.substring(emailHashCode.length() - AUTHOR_NAME_DEFAULT_LENGTH));
        return commentAuthorRepository.save(newAuthor);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyToTheComment(Long postId, Long parentCommentId, CommentFormDTO commentFormDTO) {

        if (commentFormDTO.getEmail().equals(ADMIN_EMAIL) && !SecurityUtil.isCurrentUserInRole(ROLE_ADMIN)) {
            // TODO: Not good design, need to refactor.
            return ;
        }

        CommentAuthor author = commentAuthorRepository
                .findOptionalByEmail(commentFormDTO.getEmail())
                .orElseGet(() -> createNewAuthorByEmail(commentFormDTO.getEmail()));

        Long replyToAuthorId = commentFormDTO.getReplyToAuthorId();

        Comment comment = new Comment();
        comment.setContent(commentFormDTO.getContent());
        comment.setCommentType(CommentType.REPLY_TO_COMMENT);
        comment.setCommentStatus(getCommentStatusByAuthorRole(author.getRole()));
        comment.setReplyCount(0L);

        commentRepository.save(comment);
        commentRepository.updateCommentDetail(comment.getId(), postId, author.getId(), replyToAuthorId, parentCommentId);

        // TODO: Notify target author by email.
    }

    @Override
    public Page<CommentDTO> listPostComments(Long postId, Pageable pageable) {
        return commentRepository
                .findAllByPostIdAndCommentTypeAndCommentStatus(postId, CommentType.REPLY_TO_POST, CommentStatus.ACTIVE, pageable)
                .map(dtoModelMapper::convertToDTO);
    }

    @Override
    public Page<ReplyToCommentDTO> listRepliesToComment(Long postId, Long parentCommentId, Pageable pageable) {
        return commentRepository
                .findAllByPostIdAndParentCommentIdAndCommentStatus(postId, parentCommentId, CommentStatus.ACTIVE, pageable)
                .map(dtoModelMapper::convertToReplyDTO);
    }

    private CommentStatus getCommentStatusByAuthorRole(CommentAuthorRole role) {
        boolean isAdminOrNormalRole = role.equals(CommentAuthorRole.NORMAL) || role.equals(CommentAuthorRole.ADMIN);
        return isAdminOrNormalRole
                ? CommentStatus.ACTIVE
                : CommentStatus.BLOCK;
    }
}
