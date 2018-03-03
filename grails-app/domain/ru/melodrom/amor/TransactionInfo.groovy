package ru.melodrom.amor

import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includeNames = true, includePackage = false)
class TransactionInfo implements Serializable {

    private static final long serialVersionUID = 1

    static mapWith = 'none'

    String address
    Date date
    Long amount

}
