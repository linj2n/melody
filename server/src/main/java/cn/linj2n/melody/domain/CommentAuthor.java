package cn.linj2n.melody.domain;

import cn.linj2n.melody.domain.enumeration.CommentAuthorRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table(name = "comment_author")
@Getter
@Setter
public class CommentAuthor {
    /**
     * 作者主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 作者名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 作者 md5 码，通过 email 映射生成
     */
    @Column(name = "md5_code")
    private String md5Code;

    /**
     * 作者邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 作者的角色，简单分为管理员、普通用户与匿名访问者
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private CommentAuthorRole role;

    /**
     * 作者的头像链接
     */
    @Column(name = "avatar")
    private String avatar;

    /**
     * 作者实体的创建时间
     */
    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = ZonedDateTime.now();
    }
}
