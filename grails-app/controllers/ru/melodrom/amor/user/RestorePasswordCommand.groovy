package ru.melodrom.amor.user

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includeNames = true, includePackage = false)
class RestorePasswordCommand implements Validateable {

    String email

    static constraints = {
        email email: true, blank: false, nullable: false
    }

}
