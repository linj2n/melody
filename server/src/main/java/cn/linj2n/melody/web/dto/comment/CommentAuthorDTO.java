package cn.linj2n.melody.web.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentAuthorDTO {

    @JsonProperty(value = "id")
    private long id;

    @JsonProperty(value = "avatar")
    private String avatar;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "is_post_author")
    private boolean postAuthor;
}
