package ru.melodrom.amor.security

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import grails.util.Environment
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.encoding.PasswordEncoder
import ru.melodrom.amor.WalletService
import ru.melodrom.amor.utils.LogbackArguments

@GrailsCompileStatic
@Slf4j
class UserService {

    SpringSecurityService springSecurityService

    RoleUserService roleUserService

    WalletService walletService

    @Transactional(readOnly = true)
    Integer count() {
        AppUser.count()
    }

    @Transactional
    AppUser create(String email, String password, Boolean active = false, String firstName = null, String lastName = null) {
        assert email?.trim()?.toLowerCase(): 'email can not be null or empty'
        assert EmailValidator.getInstance(false).isValid(email): 'email is wrong'
        assert password?.trim()

        email = email.trim().toLowerCase()
        password = password.trim()

        AppUser user = new AppUser(
                email: email,
                password: password,
                firstname: firstName,
                lastname: lastName,
                enabled: true,
                activated: active
        ).save(flush: true, failOnError: true)

        log.info("Created ${user}, {} {}", user.id, LogbackArguments.email(email))

        // append ROLE_INVESTOR to user
        roleUserService.add(user, RoleName.ROLE_INVESTOR.name())

        return user
    }

    @Transactional
    AppUser getById(Long userId) {
        assert userId

        return AppUser.get(userId)
    }

    @Transactional
    AppUser findByEmail(String email) {
        assert email?.trim()?.toLowerCase(): 'email can not be null or empty'
        assert EmailValidator.getInstance(false).isValid(email): 'email is wrong'

        email = email?.trim()?.toLowerCase()

        return AppUser.findByEmail(email)
    }

    @Transactional(readOnly = true)
    String getName(AppUser user) {
        assert user

        String name = "${user.firstName?.trim()} ${user.lastName?.trim()}".trim() ?: null

        if (!name)
            name = user.username

        return name
    }

    @Transactional
    String generateNewPassword(AppUser user) {
        assert user && user.id: '`user` can not be null or can not be unsaved'

        String newPassword = generateRandomPassword()?.trim()

        assert newPassword

        user.password = newPassword

        user.save(flush: true, failOnError: true)

        log.info("Updated password to: ${Environment.current == Environment.PRODUCTION ? '******' : newPassword}, {}", user.id)

        return newPassword
    }

    @Transactional
    AppUser updatePassword(AppUser user, String oldPassword, String newPassword) throws BadCredentialsException {
        assert user
        assert oldPassword
        assert newPassword?.trim()

        newPassword = newPassword?.trim()

        PasswordEncoder passwordEncoder = (PasswordEncoder) springSecurityService?.passwordEncoder

        Boolean passwordValid = passwordEncoder.isPasswordValid(user.password, oldPassword, null)

        if (!passwordValid) {
            throw new BadCredentialsException('Old password not match')
        }

        user.password = newPassword

        user.save(flush: true, failOnError: true)

        log.info("Updated password to: ${Environment.current == Environment.PRODUCTION ? '******' : newPassword}, {}", user.id)

        return user
    }

    @Transactional
    AppUser setEmailAndPassword(AppUser user, String email, String password) {
        assert user?.id
        assert email
        assert password?.trim()

        password = password.trim()

        user.email = email
        user.password = password
        user.save(flush: true, failOnError: true)

        return user
    }


    String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 10).replaceAll('-', '').trim()
    }

    RoleName getFirstRoleName(final AppUser user) {
        assert user

        List<String> authorities = user.getAuthorities().collect { it.authority } as List
        return authorities.get(0) as RoleName

    }

    @Transactional
    @CompileStatic(TypeCheckingMode.SKIP)
    List<AppUser> list(RoleName roleName) {
        assert roleName

        Role role = Role.findByAuthority(roleName.name());

        return role ? UserRole.findAllByRole(role).collect { it.user } : []
    }

    @Transactional
    AppUser activate(AppUser user) {
        assert user

        user.activated = true
        user.save(flush: true, failOnError: true)

        log.info("Activate ${user.email} {} {}", user.id, LogbackArguments.email(user.email))

        return user
    }

}
