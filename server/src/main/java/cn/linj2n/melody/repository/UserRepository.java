package cn.linj2n.melody.repository;

import cn.linj2n.melody.domain.User;
import cn.linj2n.melody.security.oauth2.UserSourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findOneByActivationKey(String key);

    Optional<User> findOneByLogin(String login);

    Optional<User> findOneByLoginOrEmail(String login, String email);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByResetCode(String code);

    Optional<User> findOneByLoginAndSourceType(String login, UserSourceType userSourceType);
}
