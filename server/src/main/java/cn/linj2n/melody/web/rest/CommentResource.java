package cn.linj2n.melody.web.rest;


import cn.linj2n.melody.service.CommentService;
import cn.linj2n.melody.web.dto.comment.CommentAuthorDTO;
import cn.linj2n.melody.web.dto.comment.CommentFormDTO;
import cn.linj2n.melody.web.utils.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api")
public class CommentResource {

    private static final Logger logger = LoggerFactory.getLogger(CommentResource.class);

    private CommentService commentService;

    @Autowired
    public CommentResource(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping(
            value = "/v1/posts/{postId}/comments",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> listPostCommentsByPostId(@PathVariable(value = "postId") Long postId, Pageable pageable) {
        return new ResponseEntity<>(
                ResponseBuilder.buildSuccessResponse(null, commentService.listPostComments(postId, pageable)),
                HttpStatus.OK
        );
    }


    @RequestMapping(
            value = "/v1/posts/{postId}/comments",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> replyToPost(@PathVariable(value = "postId") Long postId,
                                         @Valid @RequestBody CommentFormDTO commentFormDTO) {
        commentService.replyToThePost(postId, commentFormDTO);
        return new ResponseEntity<>(
                ResponseBuilder.buildSuccessResponse("评论提交成功，审核中。", null),
                HttpStatus.OK
        );
    }

    @RequestMapping(
            value = "/v1/posts/{postId}/comments/{commentId}/replies",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> listRepliesToCommentByCommentId(@PathVariable(value = "postId") Long postId,
                                                             @PathVariable(value = "commentId") Long commentId,
                                                             Pageable pageable) {
        return new ResponseEntity<>(
                ResponseBuilder.buildSuccessResponse(null, commentService.listRepliesToComment(postId, commentId, pageable)),
                HttpStatus.OK
        );
    }

    @RequestMapping(
            value = "/v1/posts/{postId}/comments/{commentId}/replies",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> replyToComment(@PathVariable(value = "postId") Long postId,
                                            @PathVariable(value = "commentId") Long commentId,
                                            @Valid @RequestBody CommentFormDTO commentFormDTO) {
        commentService.replyToTheComment(postId, commentId, commentFormDTO);
        return new ResponseEntity<>(
                ResponseBuilder.buildSuccessResponse("评论成功，审核中。", null),
                HttpStatus.OK
        );
    }

}
