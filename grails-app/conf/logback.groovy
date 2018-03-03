import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ru.melodrom.amor.helpers.Config
import grails.util.BuildSettings
import grails.util.Environment
import net.logstash.logback.appender.LoggingEventAsyncDisruptorAppender
import net.logstash.logback.encoder.LogstashEncoder
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

File targetDir = BuildSettings.TARGET_DIR

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')

        pattern =
                '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
                        '%clr(%5p) ' + // Log level
                        '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                        '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                        '%m%n%wex' // Message
    }
}

if (Environment.current.equals(Environment.PRODUCTION) || Environment.current.equals(Environment.CUSTOM)) {
    appender('stash', LoggingEventAsyncDisruptorAppender) {
        waitStrategyType = 'sleeping'
        appender("FILE", FileAppender) {
            file = '/var/log/amor.log'
            append = false
            encoder(LogstashEncoder)
        }
    }
}

logger('com.zaxxer.hikari.HikariConfig', DEBUG)
logger('grails.boot.GrailsApp', INFO)
logger('com.vladmihalcea', INFO)
logger('com.zaxxer', INFO)

logger('grails.app.conf.ru.melodrom.amor', DEBUG)
logger('grails.app.controllers.ru.melodrom.amor', DEBUG)
logger('grails.app.services.ru.melodrom.amor', DEBUG)
logger('grails.app.taglib.ru.melodrom.amor', DEBUG)
logger('grails.app.domain.ru.melodrom.amor', DEBUG)
logger('grails.app.init.ru.melodrom.amor', DEBUG)
logger('grails.app.jobs.ru.melodrom.amor', DEBUG)
logger('ru.melodrom.amor', DEBUG)
// remove loggers
logger('org.gradle.process', INFO)
logger('org.gradle.workers', INFO)
logger('org.gradle.cache', INFO)

// for debug Grails Spring Security Core plugin
//logger('grails.plugin.springsecurity.web.filter.DebugFilter', DEBUG)
//}

// for debug SQL queries
if (Config.logSql) {
    println 'Enable logging all SQL queries'
    logger('net.ttddyy.dsproxy', DEBUG)
}

if (Config.debugLogs) {
    println 'Enable crazy DEBUG logging'
}

if (Environment.isDevelopmentMode() || Environment.current.equals(Environment.TEST)) {
    if (targetDir && targetDir.canWrite()) {
        appender('FULL_STACKTRACE', FileAppender) {
            file = "${targetDir}/stacktrace.log"
            append = true
            encoder(PatternLayoutEncoder) {
                pattern = '%level %logger - %msg%n'
            }
        }
        logger('StackTrace', ERROR, ['FULL_STACKTRACE'], false)

        root(Config.debugLogs ? DEBUG : ERROR, ['STDOUT', 'FULL_STACKTRACE'])
    } else {
        root(Config.debugLogs ? DEBUG : ERROR, ['STDOUT'])
    }
}

if (Environment.current.equals(Environment.PRODUCTION) || Environment.current.equals(Environment.CUSTOM)) {
    root(Config.debugLogs ? DEBUG : ERROR, ['STDOUT', 'stash', 'FILE'])
}
