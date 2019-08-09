package cn.linj2n.melody.domain;

import cn.linj2n.melody.domain.enumeration.CommentStatus;
import cn.linj2n.melody.domain.enumeration.CommentType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment {

    /**
     * Comment id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Comment content
     */
    @NotNull
    @Size(max = 65535)
    @Type(type = "text")
    @Column(name = "content", length = 65535, nullable = false)
    private String content;

    /**
     * Comment status: { BLOCK, ACTIVE }
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommentStatus commentStatus;

    /**
     * Comment type: { REPLY_TO_POST, REPLY_TO_COMMENT }
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CommentType commentType;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    private CommentAuthor author;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    private CommentAuthor replyToAuthor;

    @Column(name = "reply_count", nullable = false)
    private Long replyCount;

    /**
     * The posts which the comment belongs to
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_post_id")
    private Post post;

    /**
     * The comments of reply
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    /**
     * Created time of comment
     */
    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
    }

}

