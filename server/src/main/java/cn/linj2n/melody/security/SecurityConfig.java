package cn.linj2n.melody.security;


import cn.linj2n.melody.repository.UserRepository;
import cn.linj2n.melody.security.oauth2.ClientResources;
import cn.linj2n.melody.security.oauth2.The3rdPartyUserDetailsBuilder;
import cn.linj2n.melody.security.oauth2.The3rdPartyUserInfoTokenService;
import cn.linj2n.melody.web.filter.AuthCookieGeneratorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.filter.CompositeFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableOAuth2Client
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Autowired
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Autowired
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Autowired
    private AuthCookieGeneratorFilter authCookieGeneratorFilter;

    private static final String OAUTH2_DEFAULT_SUCCESS_URL = "/api/v1/account";

    @Autowired
    private The3rdPartyUserDetailsBuilder the3rdPartyUserDetailsBuilder;

    @Qualifier("oauth2ClientContext")
    @Autowired
    private OAuth2ClientContext oauth2ClientContext;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class).csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers("/api/blank", "/api/v1/attachments/_create", "/api/v1/account/login**")
                .and()
                .addFilterAfter(authCookieGeneratorFilter, FilterSecurityInterceptor.class)
                .exceptionHandling()
                .and()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/api/blank").permitAll()
                .antMatchers("/api/v1/account/login**").permitAll()
                .antMatchers("/api/v1/account/password_reset", "/api/v1/account/registration", "/api/v1/account/password_reset/**").permitAll()
                .antMatchers("/api/v1/account").authenticated()
                .antMatchers("/api/v1/attachments/_create").permitAll()
                .antMatchers("/api/v1/attachments/\\d+").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/api/v1/posts/**/comments/**/replies").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.THE_3RD_PARTY_USER)
                .antMatchers("/api/v1/posts/**/comments").permitAll()
                .antMatchers("/api/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .and()
                .formLogin()
                .loginProcessingUrl("/api/v1/account/authentication").permitAll()
                .defaultSuccessUrl("/api/v1/account")
                .failureHandler(ajaxAuthenticationFailureHandler).permitAll()
                .and()
                .logout()
                .logoutUrl("/api/v1/account/logout").permitAll()
                .deleteCookies("JSESSIONID","AUTH").permitAll();
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        // 开启 CORS 的 URL
                        .addMapping("/**")
                        // 允许携带 cookies
                        .allowCredentials(true)
                        // 允许那些方法进行跨域请求
                        .allowedMethods("OPTIONS", "GET", "POST","PUT","DELETE")
                        // 允许的源地址，开启 cookie 共享时必须指定具体的源地址
                        .allowedOrigins("http://127.0.0.1:9999", "http://admin.linj2n.cn")
                        // 允许请求携带的 Headers
                        .allowedHeaders("*")
                        .exposedHeaders("AUTH");
            }
        };
    }

    private Filter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilter(github(), "/api/v1/account/login/github"));
        // add other ssoFilters eg. facebook, google...
        filter.setFilters(filters);
        return filter;
    }

    private Filter ssoFilter(ClientResources client, String path) {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        filter.setRestTemplate(template);
        The3rdPartyUserInfoTokenService tokenServices = new The3rdPartyUserInfoTokenService(
                client.getResource().getUserInfoUri(), client.getClient().getClientId(), client.getUserType(), the3rdPartyUserDetailsBuilder);
        tokenServices.setRestTemplate(template);
        filter.setTokenServices(tokenServices);
        filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                this.setDefaultTargetUrl(OAUTH2_DEFAULT_SUCCESS_URL);
                super.onAuthenticationSuccess(request, response, authentication);
            }
        });

        return filter;
    }

    @Bean(name = "Github")
    @ConfigurationProperties("github")
    public ClientResources github() {
        return new ClientResources();
    }

    // Create different ClientResources beans, eg. facebook, google...
    // ...

}

