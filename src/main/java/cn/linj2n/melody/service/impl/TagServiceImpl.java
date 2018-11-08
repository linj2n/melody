package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Tag;
import cn.linj2n.melody.repository.PostRepository;
import cn.linj2n.melody.repository.TagRepository;
import cn.linj2n.melody.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService{

    private static final Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);

    private TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag createTag(String tagName) {
        return tagRepository.save(new Tag(tagName));
    }

    @Override
    @Transactional
    public void removeTagByName(String tagName) {
//       tagRepository.findOneByName(tagName)
//               .map(tag -> {
////                   tag.getPosts().forEach(e -> e.removeTag(tag));
//                   tagRepository.delete(tag);
//                   return tag;
//               });
        getTagWithPosts(tagName).map(tag -> {
            tag.getPosts().forEach(e -> e.getTags().remove(tag));
            tagRepository.delete(tag);
            return tag;
        });
    }

    @Override
    @Transactional
    public void removeTagById(Long tagId) {
        getTagWithPosts(tagId).map(tag -> {
            tag.getPosts().forEach(e -> e.getTags().remove(tag));
            tagRepository.delete(tag);
            return tag;
        });
    }

    @Override
    public Optional<Tag> getTagByName(String tagName) {
        return tagRepository.findOptionalByName(tagName);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> getTagWithPosts(String tagName) {
        return tagRepository.findOptionalByName(tagName)
                .map(u -> {
                    u.getPosts().size();
                    return u;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> getTagWithPosts(Long tagId) {
        return tagRepository.findOptionalById(tagId)
                .map(u -> {
                    u.getPosts().size();
                    return u;
                });
    }

    @Override
    public List<Tag> listAllTags() {
        return tagRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> listAllTagsWithPosts() {
        return tagRepository.findAll()
                .stream()
                .peek(tag -> tag.getPosts().size())
                .collect(Collectors.toList());
    }

    @Override
    public Tag updateTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Boolean existsById(Long tagId) {
        return tagRepository.exists(tagId);
    }
}
