package ru.melodrom.amor.security

import grails.compiler.GrailsCompileStatic
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.apache.commons.lang.builder.HashCodeBuilder

@GrailsCompileStatic
class UserRole implements Serializable {

    private static final long serialVersionUID = 1

    AppUser user
    Role role

    @CompileStatic(TypeCheckingMode.SKIP)
    boolean equals(other) {
        if (!(other instanceof UserRole)) {
            return false
        }

        other.user?.id == user?.id &&
                other.role?.id == role?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (user) builder.append(user.id)
        if (role) builder.append(role.id)
        builder.toHashCode()
    }

    static UserRole get(long userId, long roleId) {
        where {
            user == AppUser.load(userId) &&
                    role == Role.load(roleId)
        }.get()
    }

    static UserRole create(AppUser user, Role role, boolean flush = false) {
        new UserRole(user: user, role: role).save(flush: flush, insert: true)
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    @Deprecated
    static boolean remove(AppUser u, Role r, boolean flush = false) {
        int rowCount = where {
            user == AppUser.load(u.id) &&
                    role == Role.load(r.id)
        }.deleteAll()

        rowCount > 0
    }

    static void removeAll(AppUser u) {
        where {
            user == AppUser.load(u.id)
        }.deleteAll()
    }

    static void removeAll(Role r) {
        where {
            role == Role.load(r.id)
        }.deleteAll()
    }

    static mapping = {
        id composite: ['role', 'user']
        version false
    }
}