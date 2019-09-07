package cn.linj2n.melody.web.dto;

import cn.linj2n.melody.domain.Category;
import cn.linj2n.melody.domain.Tag;
import cn.linj2n.melody.domain.enumeration.PostStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
public class PostDTO {

    /**
     * 文章 id
     */
    private Long id;
    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章概要
     */
    private String summary;

    /**
     * 文章内容预览
     */

    private String contentPreview;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章封面链接
     */
    private String backgroundImgUrl;

    /**
     * 文章状态
     */
    private PostStatus status;

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
    private Set<Category> categories;

    /**
     * 文章所属的标签
     */
    private Set<Tag> tags;

    /**
     * 文章创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 文章最后的更新时间
     */
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Integer getYearOfCreation () {
        return this.createdAt.getYear();
    }

    public Month getMonthOfCreation() {
        return this.createdAt.getMonth();
    }

    public List<String> getAllTagName() {
        return tags.stream().map(Tag::getName).sorted().collect(Collectors.toList());
    }

    public List<String> getAllCategoryName() {
        return categories.stream().map(Category::getName).sorted().collect(Collectors.toList());
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
