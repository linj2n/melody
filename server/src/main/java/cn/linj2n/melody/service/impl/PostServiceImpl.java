package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.repository.PostRepository;
import cn.linj2n.melody.repository.TagRepository;
import cn.linj2n.melody.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private PostRepository postRepository;


    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public void removePost(Long id) {
        postRepository.delete(id);
    }

    @Override
    @Transactional
    public void removePostByTitle(String title) {
        // TODO:
        postRepository.findOptionalByTitle(title).map(u -> {
            postRepository.delete(u.getId());
            return u;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Post> getPost(String postTitle) {
        return postRepository.findOptionalByTitle(postTitle).map(u -> {
            u.getTags().size();
            u.getCategories().size();
            return u;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> listAllPostsWithDetails() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .peek(e -> {
                    e.getTags().size();
                    e.getCategories().size();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> listAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Post> getPost(Long id) {
        return postRepository.findOptionalById(id).map(u -> {
            u.getTags().size();
            u.getCategories().size();
            return u;
        });
    }

    @Override
    public Post createPost(){
        return postRepository.save(new Post());
    }

    @Override
    @Transactional
    public Post updatePost(Post newPost) {
        Post oldPost = postRepository.findOne(newPost.getId());
        // TODO: Do we need to check if the label or category is valid?
        newPost.setCreatedAt(oldPost.getCreatedAt());
        return postRepository.save(newPost);
    }

    @Override
    public Boolean existsById(Long postId) {
        return postRepository.exists(postId);
    }


    @Override
    public Page<Post> listPostByPage(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> getpostsByTags(List<Long> tagIds, Pageable pageable) {
        return postRepository.findAllByTags(tagIds, Long.valueOf(tagIds.size()), pageable);
    }

    @Override
    public Page<Post> findPostsByTitle(String title, Pageable pageable) {
        return postRepository.findByTitleContaining(title, pageable);
    }

    @Override
    public Page<Post> findBySearch(List<Long> tagIdList,List<Long> categoryList, String title, Pageable pageable) {
        logger.info(tagIdList.size() + ", " + categoryList.size());
        if (!categoryList.isEmpty() && !tagIdList.isEmpty()) {
            // Convert the field name of domain class to actual column name in database, eg. "createdAt" -> "created_at". Need to refactor
            PageRequest newPageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),Sort.Direction.DESC, "created_at");
            return postRepository.findBySearch(tagIdList, Long.valueOf(tagIdList.size()), categoryList, Long.valueOf(categoryList.size()), title.toLowerCase(), newPageRequest);
        } else if (!categoryList.isEmpty()){
            return postRepository.findAllByCategoriesAndTitle(categoryList, Long.valueOf(categoryList.size()), title.toLowerCase(), pageable);
        } else if (!tagIdList.isEmpty()) {
            return postRepository.findAllByTagsAndTitle(tagIdList, Long.valueOf(tagIdList.size()), title.toLowerCase(), pageable);
        }
        return postRepository.findByTitleContaining(title, pageable);
    }
}
