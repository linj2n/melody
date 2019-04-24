package cn.linj2n.melody.security;


import cn.linj2n.melody.web.utils.ResponseBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class AjaxLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
        implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonResult = objectMapper.writeValueAsString(ResponseBuilder.buildSuccessResponse("logout successfully"));
//        response.setContentType("application/json");
//        response.getWriter().write(jsonResult);
//        response.setStatus(HttpServletResponse.SC_OK);

    }
}

