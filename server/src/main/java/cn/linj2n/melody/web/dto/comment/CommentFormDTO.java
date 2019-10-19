package cn.linj2n.melody.web.dto.comment;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentFormDTO {

    @Email
    private String email;

    private Long replyToAuthorId;

    @NotNull
    @Size(min = 8, max = 1000)
    private String content;
}
