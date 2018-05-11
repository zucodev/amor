package ru.melodrom.amor.web

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import ru.melodrom.amor.security.TokenService

import javax.servlet.http.Cookie
import javax.servlet.http.HttpSession

import static net.logstash.logback.argument.StructuredArguments.kv

@CompileStatic
@Slf4j
class CorsInterceptor {

    CorsInterceptor() {
        matchAll()
    }

    boolean before() {
        if (request.method == "OPTIONS") {
            response.setHeader("Access-Control-Allow-Origin", "*")
            response.setHeader("Access-Control-Request-Headers", "*")
            response.setHeader("Access-Control-Allow-Credentials", "true")
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
            response.setHeader("Access-Control-Max-Age", "3600")

            response.status = 200
        }

        return true
    }

    boolean after() { true }
}
