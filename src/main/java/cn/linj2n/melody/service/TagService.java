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
     * 删除标签
     * @param tagName 标签名称
     */
    void removeTagByName(String tagName);

    /**
     * 获取标签
     * @param tagName 标签名称
     * @return 仅返回标签
     */
    Optional<Tag> getTagByName(String tagName);

    /**
     * 获取标签，包括该标签所对应的文章
     * @param tagName 标签名称
     * @return 返回标签及其文章
     */
    Optional<Tag> getTagWithPosts(String tagName);

    /**
     * 获取所有的标签
     * @return 返回所有的标签
     */
    List<Tag> listTags();

    /**
     * 获取所有的标签，包括标签所对应的文章
     * @return 返回所有标签及其对应的文章
     */
    List<Tag> listTagsWithPosts();

}
