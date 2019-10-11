package cn.linj2n.melody.security.oauth2;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Map;
import java.util.Set;

public class The3rdPartyUserInfoTokenService implements ResourceServerTokenServices{

    private The3rdPartyUserDetailsBuilder the3rdPartyUserDetailsBuilder;

    private UserSourceType userSourceType;

    private UserInfoTokenServices userInfoTokenServices;

    public The3rdPartyUserInfoTokenService(String userInfoEndpointUrl, String clientId, UserSourceType userSourceType, The3rdPartyUserDetailsBuilder the3rdPartyUserDetailsBuilder) {
        this.userSourceType = userSourceType;
        this.the3rdPartyUserDetailsBuilder = the3rdPartyUserDetailsBuilder;
        this.userInfoTokenServices = new UserInfoTokenServices(userInfoEndpointUrl, clientId);
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        OAuth2Authentication authentication = userInfoTokenServices.loadAuthentication(accessToken);
        The3rdPartyUserDetails principal = the3rdPartyUserDetailsBuilder.build(getMap(authentication), userSourceType);
        Set<GrantedAuthority> authorities = principal.getAuthorities();
        authorities.addAll(authentication.getAuthorities());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal, "N/A", authorities);
        token.setDetails(authentication.getDetails());
        return new OAuth2Authentication(authentication.getOAuth2Request(), token);
    }

    @SuppressWarnings({ "unchecked" })
    private Map<String, Object> getMap(OAuth2Authentication auth2Authentication) {
        return (Map)auth2Authentication.getUserAuthentication().getDetails();
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return userInfoTokenServices.readAccessToken(accessToken);
    }

    public void setTokenType(String tokenType) {
        userInfoTokenServices.setTokenType(tokenType);
    }

    public void setRestTemplate(OAuth2RestOperations restTemplate) {
        userInfoTokenServices.setRestTemplate(restTemplate);
    }

    public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
        userInfoTokenServices.setAuthoritiesExtractor(authoritiesExtractor);
    }

    public void setPrincipalExtractor(PrincipalExtractor principalExtractor) {
        userInfoTokenServices.setPrincipalExtractor(principalExtractor);
    }

}
