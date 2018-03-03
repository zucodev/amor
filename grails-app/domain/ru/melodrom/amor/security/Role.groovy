package ru.melodrom.amor.security

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Role {

    String authority
    RoleName type

    static mapping = {
        id generator: 'native', params: [sequence_name: 'seq_role', sequence: 'seq_role'], defaultValue: "nextval('seq_role')"

        cache true
    }

    static constraints = {
        authority blank: false, unique: true
        type nullable: true
    }
}
