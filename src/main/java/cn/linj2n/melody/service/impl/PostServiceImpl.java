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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public List<Post> listPosts() {
        return postRepository.findAll()
                .stream()
                .peek(e -> {
                    e.getTags().size();
                    e.getCategories().size();
                })
                .collect(Collectors.toList());
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
}
