package cn.linj2n.melody.web.dto.comment;

import cn.linj2n.melody.domain.enumeration.CommentAuthorRole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

@Getter
@Setter
public class CommentAuthorInfoDTO {

    private long id;

    private String name;

    private String code;

    @Email
    private String email;


    private CommentAuthorRole role;

    private String avatar;

}
