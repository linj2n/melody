package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.repository.PostRepository;
import cn.linj2n.melody.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private static final int BATCH_SIZE = 20;

    @Autowired
    private PostRepository postRepository;


    @Resource(name = "redisCountingTemplate")
    HashOperations<String, String, Integer> counting;

    @Resource(name = "redisListTemplate")
    SetOperations<String, Long> postsCache;

    private static final String CACHE_POST_VIEWS = "post_views";

    private static final String CACHE_POST_ID = "post_id";

    private void loadPostDetails(Long postId) {
        Post post = postRepository.findOne(postId);
        counting.putIfAbsent(CACHE_POST_VIEWS, postId.toString(), post.getViews().intValue());
    }

    private Integer getPostViews(Long postId) {
        Integer views = counting.get(CACHE_POST_VIEWS, postId.toString());
        if (views == null) {
            loadPostDetails(postId);
            return counting.get(CACHE_POST_VIEWS, postId.toString());
        }
        return views;
    }

    @Override
    public void increasePostViews(Long postId) {
        logger.debug("Request to increase post views for id :[{}].", postId);
        postsCache.add(CACHE_POST_ID, postId);
        counting.increment(CACHE_POST_VIEWS, postId.toString(), 1L);
    }

    /**
     * Use Spring scheduling framework to save counting information Schedule time: every 10 seconds
     */
    @Scheduled(fixedRate = 10000)
    public void savePostFromCacheInBatch() {
        logger.debug("Scheduled to save post information, size={}. " + BATCH_SIZE);
        savePostFromCache(BATCH_SIZE);
    }

    private void savePostFromCache(int size) {
        logger.debug("Start to save post information in batch [size:{}]. " + size);
        int cacheSize = postsCache.size(CACHE_POST_ID).intValue();
        if (cacheSize == 0) {
            return ;
        }
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < size && i < cacheSize; i ++) {
            ids.add(postsCache.pop(CACHE_POST_ID));
        }

        for (Long id: ids) {
            updatePost(id, counting.get(CACHE_POST_VIEWS, id.toString()));
        }
    }

    @PreDestroy
    public void destroy() {
        logger.debug("Closing post service, try to save all cache information.");
        savePostFromCache(Integer.MAX_VALUE);
    }


    @Override
    @Transactional
    public void updatePost(Long postId, long views) {
        postRepository.updatePostViews(postId, views);
    }

    @Override
    @Transactional
    public void removePost(Long id) {
        postRepository.delete(id);
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
    @Transactional
    public List<Post> getPostsByTags(List<Long> tagIds) {
        return null;
    }


    @Override
    public Page<Post> listPostByPage(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> findPostsByTitle(String title, Pageable pageable) {
        return postRepository.findByTitleContaining(title, pageable);
    }

    @Override
    public Page<Post> findBySearch(List<Long> tagIdList,List<Long> categoryList, String title, Pageable pageable) {
        logger.debug(tagIdList.size() + ", " + categoryList.size());
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
