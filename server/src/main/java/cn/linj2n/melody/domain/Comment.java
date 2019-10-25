package cn.linj2n.melody.domain;

import cn.linj2n.melody.domain.enumeration.CommentStatus;
import cn.linj2n.melody.domain.enumeration.CommentType;
import cn.linj2n.melody.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
     * 评论主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 评论的内容
     */
    @NotNull
    @Size(max = 65535)
    @Type(type = "text")
    @Column(name = "content", length = 65535, nullable = false)
    private String content;

    /**
     * 评论的状态，包括 BLOCK, ACTIVE
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommentStatus commentStatus;

    /**
     * 评论的类型，包括 REPLY_TO_COMMENT, REPLY_TO_POST
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CommentType commentType;

    /**
     * 评论所属作者
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    private User author;

    /*a*
     * 评论对象的作者
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    private User replyToAuthor;

    /**
     * 评论的回复数
     */
    @Column(name = "reply_count", nullable = false)
    private Long replyCount;

    /**
     * 评论所属的文章，指出评论贴在哪篇文章下
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_post_id")
    private Post post;

    /**
     * 评论所属的父评论，指出子评论贴在那条评论下
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    /**
     * 评论的创建时间
     */
    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = DateUtil.nowDateTime();
    }

}

