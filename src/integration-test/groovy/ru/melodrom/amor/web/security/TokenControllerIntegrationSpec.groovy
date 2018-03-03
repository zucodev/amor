package ru.melodrom.amor.web.security

import ru.melodrom.amor.constants.TokenNames
import ru.melodrom.amor.utils.traits.IntegrationTestWithRestTrait
import grails.plugins.rest.client.RestResponse
import grails.test.mixin.integration.Integration
import org.springframework.http.HttpStatus
import spock.lang.Specification

@Integration
class TokenControllerIntegrationSpec extends Specification implements IntegrationTestWithRestTrait {

    void "test response all tokens"() {
        when:
        RestResponse response = restGet('session')

        then:
        response.statusCode == HttpStatus.OK
        response.json != null
        response.json.tokens != null
        response.json.tokens.size() == 1
        response.json.tokens[TokenNames.REGISTRATION] != null
        response.json.sessionId != null
    }

}
