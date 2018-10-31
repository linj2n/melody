package cn.linj2n.melody.domain;

import cn.linj2n.melody.core.AbstractTimedModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
     * 文章 url
     */
    private String url;

    /**
     * 文章查看次数
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

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }


    protected Post() {
    }

    public Post(String title) {
        this.title = title;
    }

    public Post(String title, String content, String status, String url, Long views) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.url = url;
        this.views = views;
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

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", url='" + url + '\'' +
                ", views=" + views +
                ", categories=" + categories +
                ", tags=" + tags +
                '}';
    }
}
