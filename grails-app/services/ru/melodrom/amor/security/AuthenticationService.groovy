package ru.melodrom.amor.security

import ru.melodrom.amor.constants.CookieNames
import grails.plugin.springsecurity.SpringSecurityService
import grails.util.Holders
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.beans.factory.BeanInitializationException
import org.springframework.beans.factory.InitializingBean
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices
import ru.melodrom.amor.utils.GrailsHelper

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@CompileStatic
class AuthenticationService implements InitializingBean {

    SpringSecurityService springSecurityService
    TokenBasedRememberMeServices rememberMeServices

    // variables
    String cookieDomain
    String cookiePath

    void reauthenticate(AppUser user, Boolean updateSessionCookie = true) {
        assert user

        reauthenticate(user.email, null, updateSessionCookie)
    }

    void reauthenticate(String username, String password, Boolean updateSessionCookie = true) {
        assert username: 'username can not be null'

        springSecurityService.reauthenticate username, password

        if (updateSessionCookie) {
            String sessionId = GrailsHelper.currentRequest.session.id

            HttpServletResponse response = GrailsHelper.currentResponse

            Cookie registrationCookie = new Cookie(CookieNames.session, sessionId)
            registrationCookie.setDomain(cookieDomain)
            registrationCookie.setPath(cookiePath)
            registrationCookie.setHttpOnly(true)
            response.addCookie(registrationCookie)
        }

        rememberMeServices.onLoginSuccess(GrailsHelper.currentRequest, GrailsHelper.currentResponse,
                springSecurityService.getAuthentication())
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    @Override
    void afterPropertiesSet() throws Exception {
        cookieDomain = Holders.config.server?.session?.cookie?.domain
        cookiePath = Holders.config.server?.session?.cookie?.path

        if (!cookieDomain)
            throw new BeanInitializationException("`server.session.cookie.domain` is empty or wrong: $cookieDomain")

        if (!cookiePath)
            throw new BeanInitializationException("`server.session.cookie.path` is empty or wrong: $cookiePath")
    }

}
