package cn.linj2n.melody.security.oauth2;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

public class ClientResources {

    @NestedConfigurationProperty
    private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

    @NestedConfigurationProperty
    private ResourceServerProperties resource = new ResourceServerProperties();

    private String usernameKey;

    private String loginKey;

    private String emailKey;

    private String avatarUrlKey;

    private String urlKey;

    private UserSourceType userType;

    public UserSourceType getUserType() {
        return userType;
    }

    public void setUserType(UserSourceType userType) {
        this.userType = userType;
    }

    public String getUsernameKey() {
        return usernameKey;
    }

    public void setUsernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
    }

    public String getLoginKey() {
        return loginKey;
    }

    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey;
    }

    public String getEmailKey() {
        return emailKey;
    }

    public void setEmailKey(String emailKey) {
        this.emailKey = emailKey;
    }

    public String getAvatarUrlKey() {
        return avatarUrlKey;
    }

    public void setAvatarUrlKey(String avatarUrlKey) {
        this.avatarUrlKey = avatarUrlKey;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public AuthorizationCodeResourceDetails getClient() {
        return client;
    }

    public ResourceServerProperties getResource() {
        return resource;
    }

}
