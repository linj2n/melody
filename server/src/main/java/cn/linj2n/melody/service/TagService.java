package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Tag;
import java.util.List;
import java.util.Optional;

public interface TagService {

    Tag createTag(Tag newTag);

    void removeTagByName(String tagName);

    void removeTagById(Long tagId);

    Optional<Tag> getTagByName(String tagName);

    Optional<Tag> getTagById(Long tagId);

    Optional<Tag> getTagWithPosts(String tagName);

    Optional<Tag> getTagWithPosts(Long tagId);

    List<Tag> listAllTags();

    List<Tag> listAllTagsWithPosts();

    Tag updateTag(Tag tag);

    Boolean existsById(Long tagId);
}
