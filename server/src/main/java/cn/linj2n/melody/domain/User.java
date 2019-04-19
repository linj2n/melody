package cn.linj2n.melody.domain;

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
    @Pattern(regexp = "^[a-z0-9]*$|(anonymousUser)")
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
    @Size(min = 60, max = 60)
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
    @Column(name = "reset_key", length = 50)
    @JsonIgnore
    private String resetKey;

    /**
     * 用户重置码创建时间
     */
    @Column(name = "reset_date", nullable = true)
    private ZonedDateTime resetDate = null;

    /**
     * 用户头像资源 url
     */
    @Column(name = "avatar_url")
    private String avatarUrl;

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
    @Override
    public int hashCode() {
        return 31;
    }
}
