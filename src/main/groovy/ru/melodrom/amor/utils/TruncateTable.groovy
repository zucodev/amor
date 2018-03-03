package ru.melodrom.amor.utils

import grails.util.Environment
import grails.util.Holders
import groovy.util.logging.Commons
import org.springframework.beans.BeansException

@Commons
class TruncateTable {

    static void byClass(Class className) {
        if (Environment.current.equals(Environment.TEST)) {
            String tableName = getTableName(className)

            if (tableName) {
                ScSql.execute("TRUNCATE $tableName CASCADE")
            } else {
                throw new RuntimeException("Can't determine table name for class: $className")
            }
        } else {
            log.warn("Truncate not available in ${Environment.current.name} mode, only in test mode")
        }
    }

    static protected String getTableName(Class className) {
        def sessionFactory = null

        try {
            sessionFactory = Holders.applicationContext.getBean('sessionFactory')
        } catch (BeansException ignored) {
        }

        def hibernateMetadata = sessionFactory?.getClassMetadata(className)

        if (hibernateMetadata == null) {
            return null
        }

        Class SingleTableEntityPersister = this.classLoader.loadClass('org.hibernate.persister.entity.SingleTableEntityPersister')

        if (hibernateMetadata in SingleTableEntityPersister) {
            def persister = hibernateMetadata

            return persister.tableName
        } else {
            return null
        }
    }

}
