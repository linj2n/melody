package cn.linj2n.melody.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommentInfoDTO {

    private CommentAuthorInfoDTO author;

    private CommentAuthorInfoDTO replyToAuthor;

    private Long replyToComment;

    private Long postId;

    private LocalDateTime createdAt;

    @NotNull
    private String content;

}
