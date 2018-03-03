package ru.melodrom.amor.helpers

import groovy.transform.CompileStatic

@CompileStatic
class Config {

    static boolean isLogSql() {
        Boolean logSql = false

        logSql = System.getenv('AMOR_LOG_SQL') ? Boolean.parseBoolean(System.getenv('AMOR_LOG_SQL')) : logSql
        logSql = System.properties.getProperty('amor.logSql') ? System.properties.getProperty('amor.logSql') == 'true' : logSql

        return logSql
    }

    static boolean isDebugLogs() {
        Boolean debug = false

        debug = System.getenv('AMOR_DEBUG_LOGS') ? Boolean.parseBoolean(System.getenv('AMOR_DEBUG_LOGS')) : debug
        debug = System.properties.getProperty('amor.debugLogs') ? System.properties.getProperty('amor.debugLogs') == 'true' : debug

        return debug
    }

}
