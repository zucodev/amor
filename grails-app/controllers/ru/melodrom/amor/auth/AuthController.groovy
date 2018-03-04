package ru.melodrom.amor.auth

import groovy.transform.CompileStatic
import org.springframework.http.HttpStatus
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import ru.melodrom.amor.Wallet
import ru.melodrom.amor.WalletService
import ru.melodrom.amor.constants.CookieNames
import ru.melodrom.amor.constants.ErrorCodes
import ru.melodrom.amor.helpers.ObjectConverter
import ru.melodrom.amor.security.*
import ru.melodrom.amor.utils.traits.RestControllerHelperTrait

@CompileStatic
class AuthController implements RestControllerHelperTrait {

    static responseFormats = ['json']

//    static allowedMethods = [ajaxLogin: 'POST',
//                             register : 'POST',
//                             check    : 'GET']

    UserService userService
    AuthenticationService authenticationService
    ActivationCodeService activationCodeService
    WalletService walletService

    def ajaxLogin(LoginAuthCommand cmd) {
        log.debug(cmd.toString())

        cmd.password = cmd.password?.trim()

        String errorCode = null

        if (isValidCommand(cmd)) {
            AppUser user = userService.findByEmail(cmd.email)
            Wallet wallet = null

            if (user) {
                if (!user.activated) {
                    if (cmd.activationCode) {
                        ActivationCode code = activationCodeService.findByCode(cmd.activationCode)
                        if (!code || !activationCodeService.check(user, code)) {
                            errorCode = ErrorCodes.INVALID_CODE
                        } else {
                            userService.activate(user)
                            wallet = walletService.addWalletToUser(user);
                            if (!wallet) {
                                errorCode = ErrorCodes.NO_WALLET
                            }
                        }
                    } else {
                        errorCode = ErrorCodes.REQUIRE_CODE
                    }
                }
                authenticationService.reauthenticate(cmd.email, cmd.password)
            } else {
                errorCode = ErrorCodes.NOT_FOUND
            }




            Map result = [
                    user     : user ? ObjectConverter.userInfo(user) : {},
                    roles    : user ? user.getAuthorities().collect { it.authority } as Set : [],
                    errorCode: errorCode
            ]

            renderJson(result, errorCode ? HttpStatus.CONFLICT : HttpStatus.OK)
        }
    }

    def register(RegisterAuthCommand cmd) {
        log.debug(cmd.toString())

        cmd.password = cmd.password?.trim()

        if (isValidCommand(cmd)) {
            AppUser user = null
            String errorCode = null

            if (userService.findByEmail(cmd.email)) {
                errorCode = ErrorCodes.EXISTS
            } else {
                user = userService.create(cmd.email, cmd.password)
                activationCodeService.createAndSend(user)

            }

            Map result = [
                    user     : user ? ObjectConverter.userInfo(user) : {},
                    errorCode: errorCode
            ]

            renderJson(result, errorCode ? HttpStatus.CONFLICT : HttpStatus.OK)
        }
    }

    def ajaxLogout() {
        AppUser user = (AppUser) checkAuthentication()
        if (user) {
            CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(CookieNames.rememberMe);
            SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
            cookieClearingLogoutHandler.logout(request, response, null);
            securityContextLogoutHandler.logout(request, response, null);
        }
        renderJson([], HttpStatus.OK)
    }

    def check() {
        AppUser user = (AppUser) checkAuthentication()
        if (user) {
            Map result = [
                    user  : ObjectConverter.userInfo(user),
                    roles : user.getAuthorities().collect { it.authority } as Set,
                    wallet: walletService.getByUser(user)?.address
            ]


            renderJson(result, HttpStatus.OK)
        }
    }

}
