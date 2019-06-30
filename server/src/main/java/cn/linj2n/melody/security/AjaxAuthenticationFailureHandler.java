package cn.linj2n.melody.security;
import cn.linj2n.melody.web.utils.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


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

    @Autowired
    MappingJackson2HttpMessageConverter httpMessageConverter;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        Locale locale = request.getLocale();


        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        String failedMessage = "";
        if (exception.getMessage().equalsIgnoreCase("account.user.notActivated")) {
            failedMessage = messageSource.getMessage(exception.getMessage(),null,locale);
        } else {
            failedMessage = messageSource.getMessage("account.badCredentials", null, locale);
        }
        httpMessageConverter.write(
                ResponseBuilder.buildFailedResponse(failedMessage,null),
                MediaType.APPLICATION_JSON_UTF8,
                outputMessage);
        response.setStatus(HttpStatus.OK.value());
    }
}
