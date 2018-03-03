package ru.melodrom.amor.auth

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includeNames = true, includePackage = false)
class RegisterAuthCommand implements Validateable {

    String email
    String password
    String firstName
    String secondName

    // request token, to check duplicate requests
    String token

    static constraints = {
        email email: true, blank: false, nullable: false
        password blank: false, nullable: false
        firstName nullable: true
        secondName nullable: true
    }

}
