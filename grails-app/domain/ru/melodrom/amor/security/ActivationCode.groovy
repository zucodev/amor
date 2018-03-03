package ru.melodrom.amor.security

import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString
import net.kaleidos.hibernate.usertype.JsonMapType

@GrailsCompileStatic
@ToString(includes = ['id'], includeNames = true, includePackage = false)
class ActivationCode implements Serializable {

    private static final long serialVersionUID = 1

    Long id

    Date dateCreated
    Date lastUpdated

    AppUser user
    Date expireDate
    String code

    static constraints = {
        code blank: false, unique: true
        user nullable: false
    }

    static mapping = {
        id generator: 'native', params: [sequence_name: 'seq_activation_code', sequence: 'seq_activation_code'], defaultValue: "nextval('seq_activation_code')"
        attributes type: JsonMapType
    }

}
