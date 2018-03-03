package ru.melodrom.amor.security

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.util.Holders
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeCheckingMode
import org.springframework.security.core.userdetails.UserDetails

@GrailsCompileStatic
@EqualsAndHashCode(includes = 'email')
@ToString(includes = ['id', 'email'], includeNames = true, includePackage = false)
class AppUser implements Serializable, UserDetails {

    private static final long serialVersionUID = 1

    Long id
    Date dateCreated
    Date lastUpdated
    String password
    String email
    String firstName
    String lastName

    boolean enabled = true
    boolean activated = true
    boolean accountExpired = false
    boolean accountLocked = false
    boolean passwordExpired = false

    @CompileStatic(TypeCheckingMode.SKIP)
    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }

    @Override
    String getUsername() {
        return email
    }

    @Override
    boolean isAccountNonExpired() {
        return !accountExpired
    }

    @Override
    boolean isAccountNonLocked() {
        return !accountLocked
    }

    @Override
    boolean isCredentialsNonExpired() {
        return !passwordExpired
    }

    void encodePassword() {
        SpringSecurityService springSecurityService =
                Holders.applicationContext.containsBean('springSecurityService') ?
                        Holders.applicationContext.getBean(SpringSecurityService) : null

        password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }

    def beforeValidate() {
        if (email) {
            email = email.trim()?.toLowerCase()
        }
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    static constraints = {
        email email: true, blank: false, unique: true
        password blank: false, password: true
        firstName nullable: true
        lastName nullable: true
    }

    static mapping = {
        id generator: 'native', params: [sequence_name: 'seq_user', sequence: 'seq_user'], defaultValue: "nextval('seq_user')"
        password column: '`password`'
    }

}
