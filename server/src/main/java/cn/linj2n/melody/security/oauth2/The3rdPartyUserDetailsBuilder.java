package cn.linj2n.melody.security.oauth2;

import cn.linj2n.melody.domain.User;
import cn.linj2n.melody.security.AuthoritiesConstants;
import cn.linj2n.melody.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class The3rdPartyUserDetailsBuilder {

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier(value = "Github")
    private ClientResources github;

    private static final String SEPARATOR = "_";

    private static final String DEFAULT_EMPTY_VALUE = "";

    public The3rdPartyUserDetails build(Map<String, Object> map, UserSourceType userSourceType) {
        return build(map, getResource(userSourceType));
    }

    private The3rdPartyUserDetails build(Map<String, Object> map, ClientResources resources) {
        if (resources == null || map == null || map.isEmpty()) {
            return null;
        }
        The3rdPartyUserDetails details = new The3rdPartyUserDetails();

        String login = reformatLogin(getContentByKey(map, resources.getLoginKey()), resources.getUserType());
        details.setLogin(login);

        details.setEmail(getContentByKey(map, resources.getEmailKey()));
        details.setAvatarUrl(getContentByKey(map, resources.getAvatarUrlKey()));
        details.setUrl(getContentByKey(map, resources.getUrlKey()));
        details.setFrom(resources.getUserType());

        details.setAuthorities(getDefaultAuthorities(resources.getUserType()));
        updateAuthoritiesFromDB(details);

        return details;
    }

    private String getContentByKey(Map<String, Object> map, String key) {
        if (!map.containsKey(key) || map.get(key) == null) {
            return DEFAULT_EMPTY_VALUE;
        }
        return map.get(key).toString();
    }

    private void updateAuthoritiesFromDB(The3rdPartyUserDetails userDetails) {
        User userFromDB = userService.getUserByLoginAndSourceType(userDetails.getLogin(), userDetails.getFrom())
                        .orElseGet(() -> userService.createUserInformation(userDetails));
        Set<GrantedAuthority> authoritiesFromDB = userFromDB.getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toSet());
        userDetails.getAuthorities().addAll(authoritiesFromDB);
    }

    private String reformatLogin(String login, UserSourceType userSourceType) {
        return login + SEPARATOR + userSourceType.name();
    }

    private Set<GrantedAuthority> getDefaultAuthorities(UserSourceType userSourceType) {
        return new HashSet<>(AuthorityUtils.createAuthorityList(AuthoritiesConstants.THE_3RD_PARTY_USER));
    }

    private ClientResources getResource(UserSourceType type) {
        if (type.equals(UserSourceType.GITHUB_USER)) {
            return github;
        }
        return null;
    }
}
