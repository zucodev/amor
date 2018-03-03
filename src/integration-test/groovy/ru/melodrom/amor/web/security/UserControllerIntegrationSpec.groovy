package ru.melodrom.amor.web.security

import ru.melodrom.amor.constants.CookieNames
import ru.melodrom.amor.constants.TokenNames
import ru.melodrom.amor.security.AppUser
import ru.melodrom.amor.security.UserRole
import ru.melodrom.amor.utils.TruncateTable
import ru.melodrom.amor.utils.traits.IntegrationTestWithRestTrait
import grails.plugins.rest.client.RestResponse
import grails.test.mixin.integration.Integration
import org.springframework.http.HttpStatus
import spock.lang.Specification

@Integration
class UserControllerIntegrationSpec extends Specification implements IntegrationTestWithRestTrait {

    def setup() {
        TruncateTable.byClass(UserRole)
        TruncateTable.byClass(AppUser)
    }

    void "test register new user"() {
        given:
        String userEmail = 'mynewemail@email.com'
        String userHashedEmail = 'a859ac19a6170e8ad27e4688db1fb2b8'
        String userPassword = 'somepass'

        and: 'get session info'
        RestResponse response = restGet('session')
        assert response.statusCode == HttpStatus.OK

        String sessionId = response.json?.sessionId
        String sessionCookie = "${CookieNames.session}=${sessionId};"
        String registrationToken = response.json?.tokens[TokenNames.REGISTRATION]

        assert sessionId != null
        assert registrationToken != null

        when: 'register new user by email and password'
        response = restPost('user/register') {
            header('Cookie', sessionCookie)
            json {
                email = userEmail
                password = userPassword
                token = registrationToken
            }
        }

        List<HttpCookie> cookies = getCookies(response)

        then:
        response.statusCode == HttpStatus.CREATED
        response.json != null
        response.json.user instanceof Number
        response.json.user > 0
        response.json.email == userEmail
        response.json.hashedEmail == userHashedEmail

        cookies.size() == 2

        cookies[0].name == CookieNames.rememberMe
        cookies[0].domain == '/'
        cookies[0].path == '/'
        cookies[0].value != null
        cookies[0].value != ''
        cookies[0].maxAge > 0

        cookies[1].name == CookieNames.session
        cookies[1].domain == '/'
        cookies[1].path == '/'
        cookies[1].value != null
        cookies[1].value != ''
        cookies[1].maxAge == -1
    }

}
