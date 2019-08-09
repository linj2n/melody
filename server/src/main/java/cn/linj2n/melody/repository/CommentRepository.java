package cn.linj2n.melody.repository;

import cn.linj2n.melody.domain.Comment;
import cn.linj2n.melody.domain.enumeration.CommentStatus;
import cn.linj2n.melody.domain.enumeration.CommentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

    Optional<Comment> findOptionalById(Long id);

    Page<Comment> findAllByPostIdAndCommentTypeAndCommentStatus(Long id, CommentType commentType, CommentStatus commentStatus, Pageable pageable);

    Page<Comment> findAllByPostIdAndParentCommentIdAndCommentStatus(Long postId, Long parentCommentId, CommentStatus commentStatus, Pageable pageable);

    @Modifying
    @Query(
            value = " UPDATE comment SET parent_post_id = :postId, parent_comment_id = :parentCommentId, author_id = :authorId, reply_to_author_id = :replyToAuthorId" +
                    " WHERE id = :commentId "
            ,nativeQuery = true
    )
    void updateCommentDetail(@Param("commentId") Long commentId,
                             @Param("postId") Long postId,
                             @Param("authorId") Long authorId,
                             @Param("replyToAuthorId") Long replyToAuthorId,
                             @Param("parentCommentId") Long parentCommentId);
}
