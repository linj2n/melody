package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Authority;
import cn.linj2n.melody.domain.User;
import cn.linj2n.melody.repository.AuthorityRepository;
import cn.linj2n.melody.repository.UserRepository;
import cn.linj2n.melody.service.UserService;
import cn.linj2n.melody.service.utils.RandomUtil;
import cn.linj2n.melody.web.errors.ExceptionThrownInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private static final int  DEF_EXPIRATION = 60 * 5;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    private AuthorityRepository authorityRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUserInformation(String login, String password, String username, String email) {

        /* TODO: Users who have already ACTIVATED are not allowed to register */
//        if (checkIfExitUserActivatedByLoginOrEmail(login,email)) {
//            throw new UserAlreadyExistException("User has already existed. [login=" + login + ",email=" + email + "]");
//        }

        User newUser = userRepository.findOneByLoginOrEmail(login, email)
                .filter(user -> !user.getActivated())
                .orElse(new User(login));
        logger.info("new User info : {}", newUser);

        /*1. preparing for security*/
        Set<Authority> authorities = new HashSet<>();
        Optional<Authority> authority = authorityRepository.findOneByName("ROLE_ADMIN");
        String encryptedPassword = passwordEncoder.encode(password);

        /*2. set newUser properties*/
        newUser.setPassword(encryptedPassword);
        newUser.setUsername(username);
        newUser.setEmail(email);
        // new user is active
        newUser.setActivated(true);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authority.ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        logger.info("new user info : {}",newUser);

        /* 4. save new user*/
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public Optional<User> activateRegistration(String key) {
        logger.info("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key).map(user -> {
            user.setActivationKey(null);
            user.setActivated(true);
            userRepository.save(user);
            logger.info("Activated user: {}", user);
            return user;
        });
    }

    @Override
    public Optional<User> requestPasswordReset(String email) {
        return userRepository.findOneByEmail(email)
                .filter(User::getActivated)
                .map(user -> {
                    logger.info("Request password reset [email= {}].",email);
                    user.setResetCode(RandomUtil.generateResetCode());
                    user.setResetCodeCreatedTime(ZonedDateTime.now());
                    userRepository.save(user);
                    return user;
                });
    }

    @Override
    public Optional<User> requestProfileResetKey(String email) {
        return userRepository.findOneByEmail(email)
                .filter(User::getActivated)
                .map(user -> {
                    logger.info("Request to fetch profile reset key [email = {}].", email);
                    user.setResetCode(RandomUtil.generateVerificationCode());
                    user.setResetCodeCreatedTime(ZonedDateTime.now());
                    userRepository.save(user);
                    return user;
                });

    }

    @Override
    public void generateVerificationCode(User user) {
        logger.info("Request to fetch profile reset key [email = {}].", user.getEmail());
        user.setVerificationCode(RandomUtil.generateVerificationCode());
        user.setVerificationCodeCreatedTime(ZonedDateTime.now());
        userRepository.save(user);
    }

    @Override
    public boolean validateResetKey(String email, String resetKey) {
        // TODO:
//        return userRepository.findOneByEmail(email)
//                .map(user -> validateResetKey(user, resetKey))
//                .orElse(false);
        return false;
    }

    @Override
    public boolean validateVerificationCode(User user, String code) {
        if (code == null || code.isEmpty() || user.getVerificationCode() == null) {
            return false;
        }
        ZonedDateTime fiveMinutesAgo = ZonedDateTime.now().minusSeconds(DEF_EXPIRATION);
        return user.getVerificationCode().equals(code)
                && user.getVerificationCodeCreatedTime().isAfter(fiveMinutesAgo);

    }

    @Override
    public Optional<User> resetPassword(String newPassword, String resetKey) {
        return userRepository.findOneByResetCode(resetKey)
                .map(user -> {
                    logger.info("Reset password [resetKey = {}].",resetKey);
                    logger.info("Reset password [newPassword = {}].",newPassword);
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    user.setResetCode(null);
                    user.setResetCodeCreatedTime(null);
                    return user;
                });
    }

    @Override
    public void changePassword(User user, String newPassword) {
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }

    @Override
    public void changeEmail(User user, String newEmail) {
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Override
    public void changeUsername(User user, String username) {
        user.setUsername(username);
        user.setLogin(username);
        userRepository.save(user);
    }

    @Override
    public Optional<User> resetEmail(String newEmail, String resetKey) {
        if (newEmail == null || newEmail.isEmpty()) {
            throw new ExceptionThrownInService("ExceptionThrownInService");
        }
        return Optional.empty();
    }

    @Override
    public Boolean checkIfExitUserActivatedByLoginOrEmail(String login,String email) {
        User user = userRepository.findOneByLoginOrEmail(login.toLowerCase(), email.toLowerCase()).orElse(null);
        return user != null && user.getActivated();
    }

    @Override
    public Optional<User> getUserByPasswordResetKey(String key) {
        return userRepository.findOneByResetCode(key).filter(User::getActivated);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findOneByLogin(login);
    }
}
