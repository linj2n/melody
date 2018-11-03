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
     * 删除文章
     * @param id 文章 id
     */
    void removePost(Long id);

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
     * 获取所有的文章，不包含标签与分类等信息
     * @return 返回文章列表
     */
    List<Post> listAllPosts();

    /**
     * 获取所有的文章，包含标签与分类等信息
     * @return 返回文章列表
     */
    List<Post> listAllPostsWithDetails();

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

    /**
     * 查找对应 id 的文章是否存在
     * @param id
     * @return 查找结果
     */
    Boolean existsById(Long id);
}
