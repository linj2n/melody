package cn.linj2n.melody.web.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentInfoDTO {

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    @Email
    private String email;
}
