package cn.linj2n.melody.domain;

import cn.linj2n.melody.core.AbstractTimedModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post extends AbstractTimedModel {

    /**
     * 文章所属用户
     */
    @ManyToOne
    private User user;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章状态
     */
    private String status;

    /**
     * 文章资源 url
     */
    private String url;

    /**
     * 文章查看次数
     */
    private Long views;

    /**
     * 文章所属的类别
     */
    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    },fetch = FetchType.LAZY)
    @JoinTable(name = "post_category" ,
            joinColumns = {@JoinColumn(name = "psot_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    private Set<Category> categories = new HashSet<>();

    /**
     * 文章所属的标签
     */
    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    },fetch = FetchType.LAZY)
    @JoinTable(name = "post_tag" ,
            joinColumns = {@JoinColumn(name = "post_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    private Set<Tag> tags = new HashSet<>();

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getPosts().add(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getPosts().remove(this);
    }

    protected Post() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof Post)) return false;
        Post post = (Post) o;
        return getId() != null && post.getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
