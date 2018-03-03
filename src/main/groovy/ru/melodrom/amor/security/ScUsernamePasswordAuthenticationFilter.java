package ru.melodrom.amor.security;

import grails.plugin.springsecurity.SpringSecurityUtils;
import grails.plugin.springsecurity.web.authentication.GrailsUsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ScUsernamePasswordAuthenticationFilter extends GrailsUsernamePasswordAuthenticationFilter {

    private boolean postOnly = true;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();
        password = password.trim();

        if (getStoreLastUsername()) {
            // Place the last username attempted into HttpSession for views
            HttpSession session = request.getSession(false);
            if (session != null && getAllowSessionCreation()) {
                session = request.getSession();
            }

            if (session != null) {
                session.setAttribute(SpringSecurityUtils.SPRING_SECURITY_LAST_USERNAME_KEY, username);
            }
        }

        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    public void setPostOnly(boolean postOnly) {
        super.setPostOnly(postOnly);
        this.postOnly = postOnly;
    }

}
