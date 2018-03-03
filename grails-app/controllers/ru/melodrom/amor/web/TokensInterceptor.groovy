package ru.melodrom.amor.web

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import ru.melodrom.amor.security.TokenService

import javax.servlet.http.Cookie
import javax.servlet.http.HttpSession

import static net.logstash.logback.argument.StructuredArguments.kv

@CompileStatic
@Slf4j
class TokensInterceptor {

    TokenService tokenService

    TokensInterceptor() {
        matchAll()
    }

    boolean before() {
        String controller = this.controllerName
        String requestURI = request.requestURI
        String requestedSessionId = request.requestedSessionId
        Cookie[] cookies = request.cookies
        HttpSession session = request.getSession(false)

        if (requestedSessionId && session?.id && requestedSessionId != session?.id) {
            log.error("WTF! Session changed, {}\n requestedSessionId: ${requestedSessionId}\n sessionId: ${session?.id}\n cookies count: ${cookies ? cookies.length : 0}", kv('uri', requestURI))
        }

        if (session || controller) {
            // auto-create session for new requests
            if (!session)
                session = request.getSession()

            log.debug("Init tokens, {}", kv('uri', requestURI))

            tokenService.initAll(request)
        }
        true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }

}
