

package cn.linj2n.melody.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class Oauth2CientConfig {
    public Oauth2CientConfig() {
    }

    @Bean
    public OAuth2ProtectedResourceDetails github() {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setId("github");
        details.setClientId("d9bc36174a7eed64cb4a");
        details.setClientSecret("ae50cfef7a69991b340acddc3b8ed7c66bccf289");
        details.setAccessTokenUri("https://github.com/login/oauth/access_token");
        details.setUserAuthorizationUri("https://github.com/login/oauth/authorize");
        details.setTokenName("oauth_token");
        details.setAuthenticationScheme(AuthenticationScheme.query);
        details.setClientAuthenticationScheme(AuthenticationScheme.form);
        return details;
    }

    @Qualifier("githubRestTemplate")
    @Bean
    public OAuth2RestTemplate githubRestTemplate(OAuth2ClientContext clientContext) {
        OAuth2RestTemplate template = new OAuth2RestTemplate(this.github(), clientContext);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        template.setMessageConverters(Arrays.asList(converter));
        return template;
    }
}
