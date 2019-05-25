package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.web.dto.Archive;
import cn.linj2n.melody.web.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public interface PostService {

    void removePostByTitle(String title);

    void removePost(Long id);

    Optional<Post> getPost(String postTitle);

    Optional<Post> getPost(Long id);

    List<Post> listAllPosts();

    Page<Post> listPostByPage(Pageable pageable);

    List<Post> listAllPostsWithDetails();

    Post createPost();

    Post updatePost(Post newPost);

    Boolean existsById(Long postId);

    List<Post> getPostsByTags(List<Long> tagIds);

    Page<Post> findPostsByTitle(String title, Pageable pageable);

    Page<Post> findBySearch(List<Long> tagIdList,List<Long> categoryList, String title, Pageable pageable);
}
