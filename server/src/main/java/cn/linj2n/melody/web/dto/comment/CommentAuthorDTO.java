package cn.linj2n.melody.web.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentAuthorDTO {
    private long id;
    private String avatar;
    private String name;
    private boolean isPostAuthor;
}
