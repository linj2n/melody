package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    /**
     * 删除文章
     * @param postTitle 文章名称
     */
    void removePostByName(String postTitle);


    /**
     * 获取文章，包括该文章所对应的标签
     * @param postTitle 文章名称
     * @return 返回文章及其标签
     */
    Optional<Post> getPost(String postTitle);

    /**
     * 获取所有的文章，包括文章所对应的标签
     * @return 返回所有文章及其对应的标签
     */
    List<Post> listPosts();
}
