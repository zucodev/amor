package ru.melodrom.amor.helpers

import groovy.transform.CompileStatic
import ru.melodrom.amor.security.AppUser

@CompileStatic
class ObjectConverter {

    static Map userInfo(AppUser user) {
        return [
                id       : user?.id,
                email    : user?.email,
                firstName: user?.firstName,
                lastName : user?.lastName
        ]
    }
}
