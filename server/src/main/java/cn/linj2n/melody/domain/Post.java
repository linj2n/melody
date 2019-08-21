package cn.linj2n.melody.domain;

import cn.linj2n.melody.domain.enumeration.PostStatus;
import cn.linj2n.melody.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post {

    /**
     * 文章 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文章标题
     */
    @Column(name="title")
    private String title = "Untitled";

    /**
     * 文章概要
     */
    @Type(type = "text")
    private String summary;

    /**
     * 文章内容
     */
    @Type(type = "text")
    private String content;

    /**
     * 文章状态
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.DRAFT;

    /**
     * 文章 url
     */
    private String url;

    /**
     * 文章浏览次数
     */
    private Long views;

    /**
     * 文章所属的类别
     */
    @JsonIgnore
    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    },fetch = FetchType.LAZY)
    @JoinTable(name = "post_category" ,
            joinColumns = {@JoinColumn(name = "post_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    private Set<Category> categories = new HashSet<>();

    /**
     * 文章所属的标签
     */
    @JsonIgnore
    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST,
    },fetch = FetchType.LAZY)
    @JoinTable(name = "post_tag" ,
            joinColumns = {@JoinColumn(name = "post_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Comment> commentList = new ArrayList<>();

    /**
     * 文章创建时间
     */
    @Column(name = "created_at",nullable = false)
    private ZonedDateTime createdAt;

    /**
     * 文章最后的更新时间
     */
    @Column(name = "updated_at",nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = updatedAt = DateUtil.nowDateTime();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = DateUtil.nowDateTime();
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }


    public Post() {
    }

    public Post(String title) {
        this.title = title;
    }

    public Post(String title, String content, String url, Long views) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.views = views;
    }

    public Integer getYearOfCreation () {
        return this.createdAt.getYear();
    }

    public Month getMonthOfCreation() {
        return this.createdAt.getMonth();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (! (o instanceof Post)) {
            return false;
        }
        Post post = (Post) o;
        return getId() != null && post.getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("summary", summary)
                .append("content", content)
                .append("status", status)
                .append("url", url)
                .append("views", views)
                .append("categories", categories)
                .append("tags", tags)
                .append("createdAt", createdAt)
                .append("updatedAt", updatedAt)
                .toString();
    }
}
