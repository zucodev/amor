package ru.melodrom.amor.utils.traits

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.util.Holders
import org.grails.web.util.WebUtils
import org.springframework.http.HttpStatus
import ru.melodrom.amor.utils.ContentTypes
import ru.melodrom.amor.utils.DateHelper
import ru.melodrom.amor.utils.GrailsHelper

import java.time.LocalDateTime

import static ru.melodrom.amor.utils.LogbackArguments.sessId
import static ru.melodrom.amor.utils.LogbackArguments.uId
import static net.logstash.logback.argument.StructuredArguments.kv

trait RestControllerHelperTrait {

    def checkAuthentication() {
        // TODO cache
        def currentUser = Holders.applicationContext.springSecurityService.getCurrentUser()

        if (!currentUser) {
            String errorMessage = 'Unauthorized'

            renderError(errorMessage, errorMessage, [:], HttpStatus.UNAUTHORIZED)
            return null
        }

        return currentUser
    }

    String validateToken(String tokenAttribute, String tokenValueToCheck) {
        assert tokenAttribute
        assert tokenValueToCheck

        SpringSecurityService springSecurityService = Holders.applicationContext.springSecurityService

        String newToken = null

        if (session.getAttribute(tokenAttribute).equals(tokenValueToCheck)) {
            newToken = UUID.randomUUID().toString()

            log.debug("{} Set token `${tokenAttribute}`: ${newToken}, {}, {}", sessId(session.id), uId(springSecurityService.currentUserId), kv('uri', request.getRequestURI()))
            session.setAttribute(tokenAttribute, newToken)
        } else {
            // update token if it's empty
            if (!session.getAttribute(tokenAttribute)) {
                newToken = UUID.randomUUID().toString()

                log.warn("{} Token is null, set new value `${tokenAttribute}`: ${newToken}, {}, {}", sessId(session.id), uId(springSecurityService.currentUserId), kv('uri', request.getRequestURI()))
                session.setAttribute(tokenAttribute, newToken)
            }

            String requestUri = WebUtils.getForwardURI(request) ?: request.getAttribute('javax.servlet.error.request_uri')
            String errorMessage = 'Wrong token'

            log.warn("Wrong token (${session.getAttribute(tokenAttribute)} != ${tokenValueToCheck}) for session ${session.id}, URI: ${requestUri}")

            renderError('wrong_token', errorMessage,
                    [sessionId : session.id,
                     requestUri: requestUri,
                     token     : session.getAttribute(tokenAttribute)], HttpStatus.PRECONDITION_REQUIRED)

            return null
        }

        return newToken
    }

    boolean isValidCommand(cmd) {
        if (!cmd.validate() && cmd.hasErrors()) {
            Map result = [:]

            Map fields = [:]

            cmd.errors.fieldErrors.each {
                fields.put(it.field, it.code)
            }

            result['fields'] = fields

            String requestUri = WebUtils.getForwardURI(request) ?: request.getAttribute('javax.servlet.error.request_uri')

            log.warn("Bad Request: ${requestUri}, result: ${result}")

            renderJson(result, HttpStatus.BAD_REQUEST)

            return false
        } else {
            return true
        }
    }

    void renderConflict(String errorCode, String errorMessage, Map additionalFields = [:]) {
        renderError(errorCode, errorMessage, additionalFields, HttpStatus.CONFLICT)
    }

    void renderError(String errorCode, String errorMessage, Map additionalFields = [:], HttpStatus status = HttpStatus.CONFLICT) {
        assert errorCode
        assert errorMessage

        String requestUri = WebUtils.getForwardURI(request) ?: request.getAttribute('javax.servlet.error.request_uri')

        log.warn("${errorCode}: ${requestUri}")

        Map result = [
                error    : errorCode,
                message  : errorMessage,
                path     : requestUri,
                status   : status.value(),
                timestamp: DateHelper.toUnixTimestamp(LocalDateTime.now())
        ]
        result.putAll(additionalFields)

        renderJson(result, status)
    }

    void renderJson(result, HttpStatus status = null) {
        String text = result != null ? (result as JSON)?.toString() : ''

        GrailsHelper.setNoCache(response)

        render(
                status: status ? status.value() : HttpStatus.OK,
                text: text,
                contentType: ContentTypes.JSON,
                //encoding: 'utf-8'
        )
    }

    void renderNoContent() {
        render status: HttpStatus.NO_CONTENT
    }

}
