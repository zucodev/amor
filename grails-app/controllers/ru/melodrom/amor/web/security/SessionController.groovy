package ru.melodrom.amor.web.security

import ru.melodrom.amor.constants.TokenNames
import groovy.transform.CompileStatic
import ru.melodrom.amor.security.TokenService
import ru.melodrom.amor.utils.traits.RestControllerHelperTrait

@CompileStatic
class SessionController implements RestControllerHelperTrait {

    static responseFormats = ['json']

    static allowedMethods = [info: 'GET']

    TokenService tokenService

    def info() {
        Map result = [:]
        Map tokens = [:]

        TokenNames.ALL.each { tokenName ->
            String tokenValue = tokenService.getOrInit(tokenName, request)

            tokens[tokenName] = tokenValue
        }

        result.tokens = tokens
        result.sessionId = session.id

        renderJson(result)
    }

}
