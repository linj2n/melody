package cn.linj2n.melody.web.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class UserDTO {

    @NotNull
    @Size(min = 1,message = "{message.password.required}")
    private String login;

    private String username;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @NotNull
    @Size(min = 1,message = "{message.password.required}")
    private String password;

    private String avatarUrl;

    private Set<String> authorities ;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("login", login)
                .append("username", username)
                .append("email", email)
                .append("password", password)
                .append("authorities", authorities)
                .toString();
    }
}