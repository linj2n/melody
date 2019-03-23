package cn.linj2n.melody.web.rest;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.enumeration.PostStatus;
import cn.linj2n.melody.service.PostService;
import cn.linj2n.melody.web.dto.PostDTO;
import cn.linj2n.melody.web.utils.DTOModelMapper;
import cn.linj2n.melody.web.utils.ViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/api")
public class PostResource {

    private static final Logger logger = LoggerFactory.getLogger(PostResource.class);

    private PostService postService;

    private DTOModelMapper dtoModelMapper;

    private ViewUtils viewUtils;

    @Autowired
    public PostResource(PostService postService, DTOModelMapper dtoModelMapper,ViewUtils viewUtils) {
        this.postService = postService;
        this.dtoModelMapper = dtoModelMapper;
        this.viewUtils = viewUtils;
    }

    @RequestMapping(value = "/v1/posts/{postId}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDTO) {
        // TODO: complete the post status setting
        System.out.println("hit");
        postDTO.setStatus(PostStatus.PUBLISHED);
        Post post = dtoModelMapper.convertToEntity(postDTO);
        logger.info("post.content ----> {} ",post.getContent());
        postService.updatePost(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/posts/{postId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePostById(@PathVariable(value = "postId") Long postId) {
        logger.info("request to delete post[id={}]",postId);
        if (!postService.existsById(postId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        postService.removePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/posts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDTO> getListByTagsAndCategories(@RequestParam(value = "tagIds",required = false) List<Long> tagIds,
                                                 @RequestParam(value = "categoryIds",required = false) List<Long> categoryIds) {

        List<PostDTO> allPosts=new ArrayList<>();
        if (tagIds == null || tagIds.isEmpty()) {
            logger.info("get all posts --------------> ");
            postService.listAllPosts().forEach(post -> {
                allPosts.add(dtoModelMapper.convertToDTO(post));
            });
        } else {
            logger.info("get all posts by specific tags --------------> ");
            postService.getPostsByTags(tagIds).forEach(post -> {
                allPosts.add(dtoModelMapper.convertToDTO(post));
            });
        }
        return allPosts;
    }

    @RequestMapping(value = "v1/posts/page",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> testPage(Pageable pageable) {
        logger.info("pageable -----------> ",pageable.toString());
        return new ResponseEntity<>(postService.listPostByPage(pageable), HttpStatus.OK);
    }
}
