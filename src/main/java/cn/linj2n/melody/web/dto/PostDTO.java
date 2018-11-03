package cn.linj2n.melody.web.dto;

import cn.linj2n.melody.domain.Category;
import cn.linj2n.melody.domain.Tag;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

@Data
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
    private String createdAt;

    /**
     * 文章最后的更新时间
     */
    private String updatedAt;

}
