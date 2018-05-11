import ru.melodrom.amor.constants.CookieNames

import java.util.concurrent.TimeUnit

Integer port = System.properties.getProperty('grails.env') == 'dev' ? 18080 : 8080
String host = System.properties.getProperty('grails.env') == 'dev' ? 'localhost' : 'amir.melodrom.ru'

server.port = port
server.host = host
server.session.timeout = 36000
server.session.cookie.name = CookieNames.session
server.session.cookie.path = '/'
server.session.cookie.domain = host

spring.redis.host = 'localhost'
spring.redis.port = 6379

serverUrl =  "http://${host}:${port}"

grails.server.port.http = port

int postgresPort = 5432
String databaseName = System.properties.getProperty('grails.env') == 'test' ? 'amor_test' : 'amor'
String datasourceUsername = 'postgres'
String datasourcePassword = ''

databaseName = System.getenv('DATABASE_NAME') ?: databaseName
datasourceUsername = System.getenv('DATASOURCE_USERNAME') ?: datasourceUsername
datasourcePassword = System.getenv('DATASOURCE_PASSWORD') ?: datasourcePassword

String datasourceUrl = System.getenv('DATASOURCE_URL') ?: "jdbc:postgresql://127.0.0.1:$postgresPort/${databaseName}"

// @end environment variables
// grails.spring.bean.package = 'ru.melodrom.amor.config'

grails {
    controllers {
        defaultScope = 'singleton'
        upload {
            maxFileSize = 9999999999
            maxRequestSize = 9999999999
        }
    }

    plugin {
        springsecurity {
            userLookup.userDomainClassName = 'ru.melodrom.amor.security.AppUser'
            userLookup.authorityJoinClassName = 'ru.melodrom.amor.security.UserRole'
            authority.className = 'ru.melodrom.amor.security.Role'
            providerNames=['rememberMeAuthenticationProvider']
//            rest.login.active = true
//            rest.login.endpointUrl = '/api/auth/login'
//            rest.login.failureStatusCode = 401
//            rest.login.useJsonCredentials = false
//            rest.login.useRequestParamsCredentials = true
//            rest.logout.endpointUrl ='/api/auth/logout'
//            rest.token.validation.headerName ='X-Auth-Token'
//            rest.token.storage.useRedis = true
//            rest.token.storage.redis.expiration = 360000

            // remember me section
            rememberMe.cookieName = CookieNames.rememberMe
            rememberMe.cookieDomain = host
            rememberMe.alwaysRemember = true // creates cookie even if there is no `remember me` checkbox
            rememberMe.tokenValiditySeconds = TimeUnit.DAYS.toSeconds(3 * 31) // 3 months
            rememberMe.parameter = '_spring_security_remember_me'
            rememberMe.key = '_grails_'
            rememberMe.useSecureCookie = false
            rememberMe.persistent = false

            controllerAnnotations.staticRules = [
                    [pattern: '/', access: ['permitAll']],
                    [pattern: '/error', access: ['permitAll']],
                    [pattern: '/index', access: ['permitAll']],
                    [pattern: '/index.gsp', access: ['permitAll']],
                    [pattern: '/assets/**', access: ['permitAll']],
                    [pattern: '/**/js/**', access: ['permitAll']],
                    [pattern: '/**/css/**', access: ['permitAll']],
                    [pattern: '/**/images/**', access: ['permitAll']],
                    [pattern: '/favicon.ico', access: ['permitAll']],
                    [pattern: '/auth/**', access: ['permitAll']],
                    [pattern: '/user/**', access: ['permitAll']],
                    [pattern: '/lawsuit/**', access: ['permitAll']],
                    [pattern: '/doc/**', access: ['permitAll']],
                    [pattern: '/notification/**', access: ['permitAll']],
                    [pattern: '/userNotification/**', access: ['permitAll']],
                    [pattern: '/audit/**', access: ['permitAll']],
                    [pattern: '/dict/**', access: ['permitAll']],
                    [pattern: '/clause/**', access: ['permitAll']],
                    [pattern: '/api/**', access: ['permitAll']],


                    // TECH
                    [pattern: '/console/**', access: ['hasRole("ROLE_ADMIN")']],
                    [pattern: '/console', access: ['hasRole("ROLE_ADMIN")']],

                    [pattern: '/role/**', access: ['hasRole("ROLE_ADMIN")']],
            ]

            filterChain.chainMap = [
                    [pattern: '/socket.io/**', filters: 'none'],
                    [pattern: '/assets/**', filters: 'none'],
                    [pattern: '/errors/**', filters: 'none'],
                    [pattern: '/errors', filters: 'none'],
                    [pattern: '/error/**', filters: 'none'],
                    [pattern: '/error', filters: 'none'],
                    [pattern: '/**/js/**', filters: 'none'],
                    [pattern: '/**/css/**', filters: 'none'],
                    [pattern: '/**/images/**', filters: 'none'],
                    [pattern: '/**/favicon.ico', filters: 'none'],
                    [pattern: '/favicon.ico', filters: 'none'],
                    [pattern: '/**', filters: 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter']
                    //[pattern: '/api/**', filters: 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter']
                    //[pattern: '/api/**', filters: 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter']
            ]
        }
        redis {
            poolConfig {
                maxTotal = 400
                maxIdle = 400
            }
            timeout = 2000 // default in milliseconds

            port = spring.redis.port
            host = spring.redis.host
        }
    }
}

grails {
    cors {
        enabled = true
    }
}


environments {
    development {
        signifyd.checkorder = false
        send.emails = true
        features {
            extras = true
        }
        app.url='http://localhost:3000'
        base.content.dir= '/tmp/'
    }
    stage {
        signifyd.checkorder = false
        send.emails = false
        features {
            extras = true
        }
    }
    test {
        signifyd.checkorder = false
        send.emails = false
        features {
            extras = true
        }
        app.url='http://localhost:3000'
        base.content.dir= '/tmp/'
    }
    production {
        signifyd.checkorder = true
        send.emails = true
        features {
            extras = true
        }
        app.url='http://localhost:3000'
        base.content.dir= '/tmp/'
    }
}

hibernate {
    cache {
        queries = false
        use_second_level_cache = true
        use_query_cache = false
        region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
    }
}

dataSource {
    pooled = true
    jmxExport = true
    driverClassName = 'org.postgresql.Driver'
    dialect = 'net.kaleidos.hibernate.PostgresqlExtensionsDialect'
    dbCreate = 'validate'
    url = datasourceUrl
    username = datasourceUsername
    password = datasourcePassword

    String dsLogSql = System.properties.getProperty('amor.logSql')

    if (dsLogSql || System.properties.hasProperty('amor.logSql')) {
        formatSql = true
    }
}


grails.info.email = 'sites.spb@gmail.com'
