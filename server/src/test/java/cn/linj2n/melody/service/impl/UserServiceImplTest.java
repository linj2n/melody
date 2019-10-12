package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.User;
import cn.linj2n.melody.repository.UserRepository;
import cn.linj2n.melody.security.AuthoritiesConstants;
import cn.linj2n.melody.security.oauth2.The3rdPartyUserDetails;
import cn.linj2n.melody.security.oauth2.UserSourceType;
import cn.linj2n.melody.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplTest.class);

   @Autowired
   private UserService userService;

   @Autowired
   private UserRepository userRepository;

   @Test
   public void test_createUserInformation_and_getUserByLoginAndSourceType() {
      The3rdPartyUserDetails details = new The3rdPartyUserDetails();
      details.setAvatarUrl("http://peter.cn/avatar.img");
      details.setEmail("xxx@xxx.com");
      details.setFrom(UserSourceType.GITHUB_USER);
      details.setLogin("peter_GITHUB_USER");
      details.setName("peter");
      details.setAuthorities(new HashSet<>(AuthorityUtils.createAuthorityList(AuthoritiesConstants.USER)));
      details.setUrl("http://peter.cn");

      User peter = userService.createUserInformation(details);
      assertNotNull(peter.getId());
      assertTrue(peter.getEmail().equals(details.getEmail()));
      assertTrue(peter.getUsername().equals(details.getName()));
      assertTrue(peter.getLogin().equals(details.getLogin()));
      assertTrue(peter.getUrl().equals(details.getUrl()));
      assertTrue(peter.getAvatarUrl().equals(details.getAvatarUrl()));
      assertTrue(peter.getSourceType().equals(details.getFrom()));

      Set<String> authorityNames = new HashSet<>(Arrays.asList(AuthoritiesConstants.THE_3RD_PARTY_USER, AuthoritiesConstants.USER));
      peter.getAuthorities()
              .stream()
              .peek(authority -> assertTrue(authorityNames.contains(authority.getName())));

      assertTrue(userService.getUserByLoginAndSourceType(peter.getLogin(), peter.getSourceType()).isPresent());

      // delete test data in database
      peter.setAuthorities(null);
      userRepository.save(peter);
      userRepository.delete(peter.getId());
   }

}