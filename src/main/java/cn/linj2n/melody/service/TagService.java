package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Tag;
import java.util.List;
import java.util.Optional;

public interface TagService {

    /**
     * 创建标签
     * @param tagName 标签名称
     * @return 创建后的标签
     */
    Tag createTag(String tagName);

    /**
     * 根据 tagName 删除标签
     * @param tagName 标签名称
     */
    void removeTagByName(String tagName);

    /**
     * 根据 tagId 删除标签
     * @param tagId 标签 id
     */
    void removeTagById(Long tagId);

    /**
     * 获取标签
     * @param tagName 标签名称
     * @return 仅返回标签
     */
    Optional<Tag> getTagByName(String tagName);

    /**
     * 获取标签，包括该标签所对应的文章
     * @param tagName 标签名称
     * @return 返回包含文章列表的标签
     */
    Optional<Tag> getTagWithPosts(String tagName);

    /**
     * 根据 tagId 获取标签，包括该标签所对应的文章
     * @param tagId 标签 id
     * @return 返回包含文章列表的标签
     */
    Optional<Tag> getTagWithPosts(Long tagId);

    /**
     * 获取所有的标签
     * @return 返回所有的标签
     */
    List<Tag> listAllTags();

    /**
     * 获取所有的标签，包括标签所对应的文章
     * @return 返回所有标签及其对应的文章
     */
    List<Tag> listAllTagsWithPosts();

    /**
     * 更新标签
     * @param tag 需要更新的 tag
     * @return 更新后的 tag
     */
    Tag updateTag(Tag tag);

    /**
     * 查询对应 id 的标签是否存在
     * @param tagId 标签 id
     * @return 返回结果
     */
    Boolean existsById(Long tagId);
}
