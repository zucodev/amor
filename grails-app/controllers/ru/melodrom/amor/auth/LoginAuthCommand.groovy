package ru.melodrom.amor.auth

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includeNames = true, includePackage = false)
class LoginAuthCommand implements Validateable {

    String email
    String password
    String activationCode

    static constraints = {
        email email: true, blank: false, nullable: false
        password blank: false, nullable: false
        activationCode nullable: true
    }

}
