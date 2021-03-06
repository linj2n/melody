package cn.linj2n.melody.web.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentDTO {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "author")
    private CommentAuthorDTO author;

    @JsonProperty(value = "createdAt")
    private Long createdTime;

    @JsonProperty(value = "content")
    private String content;

    @JsonProperty(value = "replyCount")
    private Long replyCount;
}
