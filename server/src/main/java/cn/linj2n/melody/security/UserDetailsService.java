package cn.linj2n.melody.security;

import cn.linj2n.melody.domain.User;
import cn.linj2n.melody.repository.UserRepository;
import cn.linj2n.melody.security.UserNotActivatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService{

    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException, UserNotActivatedException {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();
        Optional<User> userFromDatabase = userRepository.findOneByLoginOrEmail(lowercaseLogin, lowercaseLogin);
        // Default locale value
        Locale locale = new Locale("zh","CN");
        return userFromDatabase.map(user -> {

            // 1. 处理 user 没有激活情况
            if (!user.getActivated()) {
                throw new UserNotActivatedException("account.user.notActivated");
            }

            // 2. 获取 user 拥有的权限
            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());

            // 3. 初始化对应的 UserDetails 对象，并返回。
            return new org.springframework.security.core.userdetails.User(lowercaseLogin,
                    user.getPassword(),
                    grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("account.user.notExisted",null,locale)));

    }
}

