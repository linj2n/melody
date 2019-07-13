package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.User;
import java.util.Optional;

public interface UserService {

    User createUserInformation(String login, String password, String username, String email);

    Optional<User> activateRegistration(String key);

    Optional<User> requestPasswordReset(String email);

    Optional<User> requestProfileResetKey(String email);

    void generateVerificationCode(User user);

    boolean validateVerificationCode(User user, String code);

    boolean validateResetKey(String email, String resetKey);

    Optional<User> resetPassword(String newPassword, String resetKey);

    void changePassword(User user, String newPassword);

    void changeEmail(User user, String newEmail);

    void changeUsername(User user, String username);

    Optional<User> resetEmail(String newEmail, String resetKey);

    Boolean checkIfExitUserActivatedByLoginOrEmail(String login,String email);

    Optional<User> getUserByPasswordResetKey(String key);

    Optional<User> getUserByLogin(String login);
}
