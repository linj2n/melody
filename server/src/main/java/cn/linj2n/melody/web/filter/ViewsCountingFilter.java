package cn.linj2n.melody.web.filter;

import cn.linj2n.melody.service.CountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


@Component
public class ViewsCountingFilter extends OncePerRequestFilter {

    private static final String[] INCLUDED_ENDPOINTS = new String[]{"/posts/**", "/archives", "/categories", "/wiki", "/about", "/index"};

    @Autowired
    private CountingService countingService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String sessionId = request.getSession(true).getId();
        countingService.increaseSiteVisitCount(sessionId);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Arrays.stream(INCLUDED_ENDPOINTS)
                .noneMatch(endpoint -> new AntPathMatcher().match(endpoint, request.getRequestURI()));
    }
}