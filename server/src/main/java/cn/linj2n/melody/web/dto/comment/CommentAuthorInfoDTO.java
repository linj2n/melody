package cn.linj2n.melody.web.dto;

import cn.linj2n.melody.domain.enumeration.CommentAuthorRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentAuthorInfoDTO {

    private Long id;

    private String name;

    private String code;

    private String email;

    private CommentAuthorRole role;

    private String avatar;

}
