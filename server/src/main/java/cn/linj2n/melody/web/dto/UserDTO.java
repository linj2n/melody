package cn.linj2n.melody.web.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

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

    private Set<String> authorities ;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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