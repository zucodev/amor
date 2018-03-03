package ru.melodrom.amor.user

import groovy.transform.CompileStatic
import org.springframework.http.HttpStatus
import ru.melodrom.amor.security.AppUser
import ru.melodrom.amor.security.UserService
import ru.melodrom.amor.utils.traits.RestControllerHelperTrait

@CompileStatic
class UserController implements RestControllerHelperTrait {

    static responseFormats = ['json']

//    static allowedMethods = [exists         : 'POST',
//                             restorePassword: 'POST']

    UserService userService

    def exists(CheckUserExistsCommand cmd) {
        log.debug(cmd.toString())

        if (isValidCommand(cmd)) {
            Map result = [:]

            AppUser user = userService.findByEmail(cmd.email)

            result.exists = user ? true : false

            renderJson(result, HttpStatus.OK)
        }
    }

    def restorePassword(RestorePasswordCommand cmd) {
        log.debug(cmd.toString())

        if (isValidCommand(cmd)) {
            AppUser user = userService.findByEmail(cmd.email)

            if (!user) {
                renderNoContent()
                return
            }

            String newPassword = userService.generateNewPassword(user)

            // TODO: send restore password event

            renderJson(null, HttpStatus.OK)
        }
    }

}
