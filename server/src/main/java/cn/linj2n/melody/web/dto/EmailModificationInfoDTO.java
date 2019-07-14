package cn.linj2n.melody.web.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EmailModificationInfoDTO {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String code;
}
