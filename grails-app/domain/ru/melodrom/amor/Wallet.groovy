package ru.melodrom.amor

import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString
import net.kaleidos.hibernate.usertype.JsonMapType
import ru.melodrom.amor.security.AppUser

@GrailsCompileStatic
@ToString(includes = ['id'], includeNames = true, includePackage = false)
class Wallet implements Serializable {

    private static final long serialVersionUID = 1

    Long id

    Date dateCreated
    Date lastUpdated

    AppUser user
    String address

    Boolean test = false
    Map attributes = [:]

    static constraints = {
        address nullable: false
        user nullable: true
    }

    static mapping = {
        id generator: 'native', params: [sequence_name: 'seq_wallet', sequence: 'seq_wallet'], defaultValue: "nextval('seq_wallet')"
        attributes type: JsonMapType
    }

}
