package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.User;
import java.util.Optional;

public interface UserService {

    User createUserInformation(String login, String password, String username, String email);

    Optional<User> activateRegistration(String key);

    Optional<User> requestPasswordReset(String email);

    Optional<User> resetPassword(String newPassword, String resetKey);

    Boolean checkIfExitUserActivatedByLoginOrEmail(String login,String email);

    Optional<User> getUserByPasswordResetKey(String key);

    Optional<User> getUserByLogin(String login);
}
