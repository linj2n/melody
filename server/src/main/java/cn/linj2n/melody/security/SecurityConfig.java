package cn.linj2n.melody.security;


import cn.linj2n.melody.repository.UserRepository;
import cn.linj2n.melody.security.AjaxAuthenticationFailureHandler;
import cn.linj2n.melody.web.filter.AuthCookieGeneratorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
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
        http
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers("/api/blank", "/api/v1/attachments/create")
                .and()
                .addFilterAfter(authCookieGeneratorFilter, FilterSecurityInterceptor.class)
                .exceptionHandling()
                .and()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/api/blank").permitAll()
                .antMatchers("/api/v1/account/password_reset").permitAll()
                .antMatchers("/api/v1/account/registration").permitAll()
                .antMatchers("/api/v1/account/password_reset/**").permitAll()
                .antMatchers("/api/v1/account").authenticated()
                .antMatchers("/admin/**").permitAll()
//                    .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .and()
                .formLogin()
                .loginProcessingUrl("/api/v1/account/authentication").permitAll()
//                    .successHandler(ajaxAuthenticationSuccessHandler)
                .defaultSuccessUrl("/api/v1/account")
                .failureHandler(ajaxAuthenticationFailureHandler).permitAll()
                .and()
                .logout()
                .logoutUrl("/api/v1/logout").permitAll()
//                    .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                .logoutSuccessUrl("/api/v1/account/logout")
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
                        .allowedOrigins("http://127.0.0.1:9999")
                        // 允许请求携带的 Headers
                        .allowedHeaders("*")
                        .exposedHeaders("AUTH");
            }
        };
    }
}

