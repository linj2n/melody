package cn.linj2n.melody.web.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentDTO {

    @JsonProperty(value = "id")
    private long id;

    @JsonProperty(value = "author")
    private CommentAuthorDTO author;

    @JsonProperty(value = "created_at")
    private long createdTime;

    @JsonProperty(value = "content")
    private String content;

    @JsonProperty(value = "child_comments_size")
    private long childCommentsSize;
}