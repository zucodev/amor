package ru.melodrom.amor.security

import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j

@GrailsCompileStatic
@Slf4j
class RoleUserService {

    @CompileStatic(TypeCheckingMode.SKIP)
    @Transactional
    List<String> findByUser(AppUser user) {
        assert user && user.id

        UserRole.findAllByUser(user)*.role.authority
    }

    @Transactional
    UserRole add(AppUser user, RoleName authority) {
        assert user
        assert authority

        add(user, authority.name())
    }

    @Transactional
    UserRole add(AppUser user, String authority) {
        assert user: '`user` can not be null'
        assert authority?.trim(): '`authority` can not be null'

        Role role = Role.findByAuthority(authority.trim())

        if (!role)
            throw new RuntimeException('Role not found, authority: ' + authority)

        log.debug("Adding ${authority}, {}", user.id)

        UserRole instance = UserRole.findOrCreateByUserAndRole(user, role)
        instance.save(flush: true, failOnError: true)
    }

}
