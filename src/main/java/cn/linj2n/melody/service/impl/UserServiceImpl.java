package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Authority;
import cn.linj2n.melody.domain.User;
import cn.linj2n.melody.repository.AuthorityRepository;
import cn.linj2n.melody.repository.UserRepository;
import cn.linj2n.melody.service.UserService;
import cn.linj2n.melody.service.utils.RandomUtil;
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

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

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
        // new user is not active
        newUser.setActivated(false);
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
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(ZonedDateTime.now());
                    userRepository.save(user);
                    return user;
                });
    }

    @Override
    public Optional<User> resetPassword(String newPassword, String resetKey) {
        return userRepository.findOneByResetKey(resetKey)
                .map(user -> {
                    logger.info("Reset password [resetKey = {}].",resetKey);
                    logger.info("Reset password [newPassword = {}].",newPassword);
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    user.setResetDate(null);
                    user.setResetKey(null);
                    return user;
                });
    }

    @Override
    public Boolean checkIfExitUserActivatedByLoginOrEmail(String login,String email) {
        User user = userRepository.findOneByLoginOrEmail(login.toLowerCase(), email.toLowerCase()).orElse(null);
        return user != null && user.getActivated();
    }

    @Override
    public Optional<User> getUserByPasswordResetKey(String key) {
        return userRepository.findOneByResetKey(key).filter(User::getActivated);
    }
}
