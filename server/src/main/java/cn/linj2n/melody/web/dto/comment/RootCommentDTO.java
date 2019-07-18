package cn.linj2n.melody.web.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RootCommentVO {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "author")
    private CommentAuthorInfoDTO author;

    @JsonProperty(value = "created_at")
    private Long createdTime;

    @JsonProperty(value = "content")
    private String content;

}
