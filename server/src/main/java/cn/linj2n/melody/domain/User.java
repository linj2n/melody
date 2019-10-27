package cn.linj2n.melody.domain;

import cn.linj2n.melody.security.oauth2.UserSourceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户登录名
     */
    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    /**
     * 用户名
     */
    @Size(max = 50)
    @Column(length = 50)
    private String username;

    /**
     * 用户密码
     */
    @JsonIgnore
    @NotNull
    @Size(min = 2, max = 60)
    @Column(length = 60)
    private String password;

    /**
     * 用户邮箱
     */
    @Email
    @Size(max = 100)
    @Column(length = 100, unique = true)
    private String email;

    /**
     * 用户激活状态
     */
    @Column(nullable = false)
    private Boolean activated;

    /**
     * 用户激活码
     */
    @Size(max = 50)
    @Column(name = "activation_key", length = 50)
    @JsonIgnore
    private String activationKey;

    /**
     * 用户重置码，用于找回密码
     */
    @Size(max = 50)
    @Column(name = "reset_code", length = 50)
    @JsonIgnore
    private String resetCode;

    /**
     * 用户重置码创建时间
     */
    @Column(name = "reset_code_created_time", nullable = true)
    private ZonedDateTime resetCodeCreatedTime= null;

    /**
     * 用户用来修改信息的验证码
     */
    @Column(name = "verification_code", nullable = true)
    private String verificationCode;

    /**
     * 用户用来修改信息的验证码的创建时间
     */
    @Column(name = "verification_code_created_time", nullable = true)
    private ZonedDateTime verificationCodeCreatedTime = null;

    /**
     * 用户头像资源 url
     */
    @Column(name = "avatar_url")
    private String avatarUrl;

    /**
     * 用户类型
     */

    @Enumerated(EnumType.STRING)
    private UserSourceType sourceType;

    /**
     * 用户主页
     */
    @Column(name = "url")
    private String url;

    /**
     * 用户所有权限集
     */
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    private Set<Authority> authorities = new HashSet<>();


    protected User() {

    }
    public User(Long id) {
        setId(id);
    }

    public User(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public ZonedDateTime getResetCodeCreatedTime() {
        return resetCodeCreatedTime;
    }

    public void setResetCodeCreatedTime(ZonedDateTime resetCodeCreatedTime) {
        this.resetCodeCreatedTime = resetCodeCreatedTime;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public ZonedDateTime getVerificationCodeCreatedTime() {
        return verificationCodeCreatedTime;
    }

    public void setVerificationCodeCreatedTime(ZonedDateTime verificationCodeCreatedTime) {
        this.verificationCodeCreatedTime = verificationCodeCreatedTime;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public UserSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(UserSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
