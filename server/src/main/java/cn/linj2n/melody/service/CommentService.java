package cn.linj2n.melody.service;

import cn.linj2n.melody.web.dto.comment.CommentDTO;
import cn.linj2n.melody.web.dto.comment.CommentFormDTO;
import cn.linj2n.melody.web.dto.comment.ReplyToCommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    void replyToThePost(Long postId, CommentFormDTO commentFormDTO);

    void replyToTheComment(Long postId, Long parentCommentId, CommentFormDTO commentFormDTO);

    Page<CommentDTO> listPostComments(Long postId, Pageable pageable);

    Page<ReplyToCommentDTO> listRepliesToComment(Long postId, Long parentCommentId, Pageable pageable);

}
