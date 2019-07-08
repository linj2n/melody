package cn.linj2n.melody.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordModificationDTO {

    private String newPassword;

    private String token;

}
