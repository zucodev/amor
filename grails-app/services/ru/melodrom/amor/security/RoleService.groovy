package ru.melodrom.amor.security

import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import groovy.util.logging.Slf4j

import static net.logstash.logback.argument.StructuredArguments.kv

@GrailsCompileStatic
@Slf4j
class RoleService {

    @Transactional
    Role create(String authority) {
        assert authority

        Role item = new Role(authority: authority)
        item.save(flush: true, failOnError: true)

        log.debug "Created ${item}"

        item
    }

    @Transactional
    Role createIfNotExist(RoleName roleName) {
        assert roleName

        createIfNotExist(roleName.name())
    }

    @Transactional
    Role createIfNotExist(String authority) {
        assert authority

        log.debug('Initializing role, {}', kv('authority', authority))

        Role item = Role.findByAuthority(authority)

        if (!item) {
            return create(authority)
        } else {
            return item
        }
    }

    @Transactional
    void initRoles() {
        log.info('Initializing roles')

        List<RoleName> roleNames = RoleName.values().toList()
        roleNames.each {
            createIfNotExist(it)
        }
    }

}
