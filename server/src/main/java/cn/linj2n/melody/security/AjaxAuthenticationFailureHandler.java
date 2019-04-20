package cn.linj2n.melody.security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class AjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(AjaxAuthenticationFailureHandler.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        Locale locale = request.getLocale();
        if (exception.getMessage().equalsIgnoreCase("account.user.notActivated")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,messageSource.getMessage(exception.getMessage(),null,locale));
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, messageSource.getMessage("account.badCredentials", null, locale));
        }
    }
}
