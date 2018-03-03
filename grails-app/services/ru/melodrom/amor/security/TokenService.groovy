package ru.melodrom.amor.security

import ru.melodrom.amor.constants.TokenNames
import grails.plugin.springsecurity.SpringSecurityService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import ru.melodrom.amor.utils.GrailsHelper

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

import static ru.melodrom.amor.utils.LogbackArguments.sessId
import static ru.melodrom.amor.utils.LogbackArguments.uId
import static net.logstash.logback.argument.StructuredArguments.kv

@CompileStatic
@Slf4j
class TokenService {

    SpringSecurityService springSecurityService

    void initAll(HttpServletRequest request = null) {
        if (!request)
            request = GrailsHelper.currentRequest

        // append tokens (init values)
        TokenNames.ALL.each { tokenName ->
            getOrInit(tokenName, request)
        }
    }

    String getOrInit(String tokenName, HttpServletRequest request = null) {
        assert tokenName

        if (!request)
            request = GrailsHelper.currentRequest

        HttpSession session = request.getSession(false)

        if (session) {
            String value = session.getAttribute(tokenName)

            if (!value) {
                value = UUID.randomUUID().toString()

                log.debug("{} Set token `${tokenName}`: ${value}, {}, {}", sessId(session.id), uId(springSecurityService.currentUserId), kv('uri', request.getRequestURI()))
                request.session.setAttribute(tokenName, value)
            }

            return value
        }

        return null
    }

}
