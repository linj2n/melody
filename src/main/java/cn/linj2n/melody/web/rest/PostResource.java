package cn.linj2n.melody.web.rest;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.service.PostService;
import cn.linj2n.melody.web.dto.PostDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api")
public class PostResource {

    private static final Logger logger = LoggerFactory.getLogger(PostResource.class);

    private PostService postService;

    private ModelMapper modelMapper;

    @Autowired
    public PostResource(PostService postService, ModelMapper modelMapper) {
        this.postService = postService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(value = "/v1/posts/{postId}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDTO) {
        Post post = modelMapper.map(postDTO,Post.class);
        postService.updatePost(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/posts/{postId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePost(PostDTO postDTO) {
        // TODO:
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
