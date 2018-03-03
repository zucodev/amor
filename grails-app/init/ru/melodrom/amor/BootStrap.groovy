package ru.melodrom.amor

import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.core.GrailsApplication
import grails.plugin.springsecurity.SecurityFilterPosition
import grails.plugin.springsecurity.SpringSecurityUtils
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Commons
import ru.melodrom.amor.security.AppUser
import ru.melodrom.amor.security.RoleName
import ru.melodrom.amor.security.RoleService
import ru.melodrom.amor.security.RoleUserService

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@GrailsCompileStatic
@Commons
class BootStrap {

    GrailsApplication grailsApplication
    RoleUserService roleUserService
    RoleService roleService
    WebSocketService webSocketService
    WalletService walletService

    def init = { servletContext ->

        initRolesAndDefaultUsers()
        initBasicJsonMarshaller();
        initJSR310JsonMarshaller()

        //SpringSecurityUtils.clientRegisterFilter('restLogoutFilter', SecurityFilterPosition.LOGOUT_FILTER.order - 1)

    }

    @CompileStatic(TypeCheckingMode.SKIP)
    private void initRolesAndDefaultUsers() {
        roleService.initRoles()

        AppUser adminUser = AppUser.findByEmail('admin@melodrom.ru') ?: new AppUser(
                password: 'admin123',
                email: 'admin@melodrom.ru',
                enabled: true, activated: true).save(failOnError: true)
        roleUserService.add(adminUser, RoleName.ROLE_ADMIN)


        AppUser investorUser = AppUser.findByEmail('test@melodrom.ru') ?: new AppUser(
                password: 'test123',
                email: 'test@melodrom.ru',
                enabled: true, activated: true).save(failOnError: true)
        roleUserService.add(investorUser, RoleName.ROLE_INVESTOR)

        String testAddress = '1Gvc4seXLjyUeVjY9J8tJeM3GFLMWgm8iD'

        Wallet wallet = walletService.getByAddress(testAddress) ?: new Wallet(
                address: testAddress,
                user: investorUser
        ).save(failOnError: true)

        webSocketService.addClient(testAddress)

    }

    static void initBasicJsonMarshaller() {
        JSON.registerObjectMarshaller Enum, { Enum someEnum ->
            someEnum.toString()
        }
    }

    static void initJSR310JsonMarshaller() {
        JSON.registerObjectMarshaller OffsetDateTime, { OffsetDateTime date ->
            DateTimeFormatter.ISO_DATE_TIME.format(date)
        }
        JSON.registerObjectMarshaller LocalDate, { LocalDate date ->
            DateTimeFormatter.ISO_DATE.format(date)
        }
    }

    def destroy = {
    }

}
