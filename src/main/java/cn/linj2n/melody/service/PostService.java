package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    /**
     * 删除文章
     * @param title 文章名称
     */
    void removePostByTitle(String title);

    /**
     * 获取文章，包括该文章所对应的标签
     * @param postTitle 文章名称
     * @return 返回文章及其标签
     */
    Optional<Post> getPost(String postTitle);

    /**
     * 获取文章，包括该文章所对应的标签
     * @param id 文章 id
     * @return 返回文章及其标签
     */
    Optional<Post> getPost(Long id);

    /**
     * 获取所有的文章，包括文章所对应的标签
     * @return 返回所有文章及其对应的标签
     */
    List<Post> listPosts();

    /**
     * 新建文章
     * @return 新建后的 Post ，被初始化的值包含 id, createdAt
     */
    Post createPost();

    /**
     * 更新文章，除 createdAt 外的域将会被更新
     * @param newPost
     * @return 更新后的 post 实体
     */
    Post updatePost(Post newPost);
}
