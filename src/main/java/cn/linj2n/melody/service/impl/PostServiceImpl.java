package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Category;
import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.Tag;
import cn.linj2n.melody.repository.CategoryRepository;
import cn.linj2n.melody.repository.PostRepository;
import cn.linj2n.melody.repository.TagRepository;
import cn.linj2n.melody.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private PostRepository postRepository;

    private TagRepository tagRepository;

    CategoryRepository categoryRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
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
        return postRepository.save(new Post(""));
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
        return postRepository.findAllByTags(tagIds,Long.valueOf(tagIds.size()));
    }

    @Override
    public Map<String, List<Post>> listAllPostsGroupByMonth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        TreeMap<String, List<Post>> postsMap = new TreeMap<>();
        postRepository.findAllByOrderByCreatedAtDesc().forEach(post -> {
            String month = post.getCreatedAt().format(formatter);
            List<Post> posts = postsMap.get(month);
            if (posts == null) {
                posts = new ArrayList<>();
            }
            posts.add(post);
            postsMap.put(month, posts);
        });
        return postsMap.descendingMap();
    }

    @Override
    public Map<String, List<Post>> listAllPostsGroupByCategory() {
//        TreeMap<String, List<Post>> postsMap = new TreeMap<>();
//        postRepository.findAllByOrderByCreatedAtDesc().forEach(post -> {
//            String categoryName = post.getCreatedAt().format(formatter);
//            List<Post> posts = postsMap.get(categoryName);
//            if (posts == null) {
//                posts = new ArrayList<>();
//            }
//            posts.add(post);
//            postsMap.put(month, posts);
//        });
//
//        return postsMap.descendingMap();


        return null;
    }

    @Override
    public Page<Post> listPostByPage(Pageable pageable) {
       return postRepository.findAll(pageable);
    }
}
