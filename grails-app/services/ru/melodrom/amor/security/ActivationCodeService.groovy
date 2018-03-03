package ru.melodrom.amor.security

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugins.mail.MailService
import grails.transaction.Transactional
import grails.util.Environment
import groovy.time.TimeCategory
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.encoding.PasswordEncoder
import ru.melodrom.amor.helpers.MailHelper
import ru.melodrom.amor.utils.LogbackArguments

@GrailsCompileStatic
@Slf4j
class ActivationCodeService {

    MailHelper mailHelper

    @Transactional
    @CompileStatic(TypeCheckingMode.SKIP)
    ActivationCode createAndSend(AppUser user, Boolean send = true) {
        assert user: 'user can not be null'


        Date expireDate = null

        use(TimeCategory) {
            expireDate = new Date() + 1.days
        }

        ActivationCode activationCode = new ActivationCode(
                user: user,
                expireDate: expireDate,
                code: UUID.randomUUID().toString()
        ).save(flush: true, failOnError: true)

        log.info("Created ${activationCode}, {} {}", user.id, LogbackArguments.email(user.email))

        mailHelper.sendNoReply('Activation code', activationCode.code, user.email)

        return activationCode
    }

    @Transactional
     Boolean check(AppUser user, ActivationCode activationCode) {
        assert user: 'user can not be null'
        assert activationCode: 'code can not be null'


        return activationCode.user == user && activationCode.expireDate > new Date()
    }

    @Transactional
    ActivationCode findByCode(String code) {
        assert code?.trim(): 'code can not be null or empty'

        return ActivationCode.findByCode(code)
    }

}
