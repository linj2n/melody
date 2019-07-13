package cn.linj2n.melody.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserNameModificationInfoDTO {

    @NotNull
    private String username;

    @NotNull
    private String code;
}
