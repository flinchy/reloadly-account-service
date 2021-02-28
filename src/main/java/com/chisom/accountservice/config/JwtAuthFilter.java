package com.chisom.accountservice.config;

import com.chisom.accountservice.config.model.UsernamePasswordAuthenticationTokenImpl;
import com.chisom.accountservice.exception.CustomException;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Chisom.Iwowo
 */
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

    protected JwtAuthFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse
    ) throws IOException {

        try {
            String authToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

            if (StringUtils.isEmpty(authToken)) {
                throw new CustomException("No Jwt token found in header", HttpStatus.UNAUTHORIZED);
            }

            UsernamePasswordAuthenticationTokenImpl token = new UsernamePasswordAuthenticationTokenImpl(authToken);
            return getAuthenticationManager().authenticate(token); //authentication manager forwards the request to the Provider
        } catch (Exception ex) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult
    ) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

}
