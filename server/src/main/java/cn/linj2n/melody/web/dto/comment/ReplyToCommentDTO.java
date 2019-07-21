package cn.linj2n.melody.web.dto.comment;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyToCommentDTO {

    @JsonProperty(value = "id")
    private long id;

    @JsonProperty(value = "author")
    private CommentAuthorDTO author;

    @JsonProperty(value = "reply_to_author")
    private CommentAuthorDTO replyToAuthor;

    @JsonProperty(value = "created_at")
    private long createdTime;

    @JsonProperty(value = "content")
    private String content;

}
