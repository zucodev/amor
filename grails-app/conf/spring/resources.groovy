import ru.melodrom.amor.helpers.MailHelper
import ru.melodrom.amor.security.ScUsernamePasswordAuthenticationFilter
import ru.melodrom.amor.security.UserDetailsService
import ru.melodrom.amor.web.mapping.UrlMappingsHolderFactoryBean
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.util.Holders
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.jooq.impl.DefaultExecuteListenerProvider
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.session.web.http.DefaultCookieSerializer
import org.springframework.web.servlet.i18n.FixedLocaleResolver

beans = {
    // for async support
    xmlns task: 'http://www.springframework.org/schema/task'
    task.'annotation-driven'('proxy-target-class': true, 'mode': 'proxy')

    mailHelper(MailHelper) {
        mailService = ref('mailService')
    }

    localeResolver(FixedLocaleResolver, Locale.US)

    // need for Spring Session (+ Redis)
    cookieSerializer(DefaultCookieSerializer) {
        cookieName = 'amor_session'
        domainName = 'amir.melodrom.ru'
        cookiePath = '/'
    }

    def conf = SpringSecurityUtils.securityConfig

    /** userDetailsService */
    userDetailsService(UserDetailsService) {
        grailsApplication = Holders.grailsApplication
        userService = ref('userService')
    }

    /** authenticationProcessingFilter **/
    authenticationProcessingFilter(ScUsernamePasswordAuthenticationFilter) {
        authenticationManager = ref('authenticationManager')
        sessionAuthenticationStrategy = ref('sessionAuthenticationStrategy')
        authenticationSuccessHandler = ref('authenticationSuccessHandler')
        authenticationFailureHandler = ref('authenticationFailureHandler')
        rememberMeServices = ref('rememberMeServices')
        authenticationDetailsSource = ref('authenticationDetailsSource')
        requiresAuthenticationRequestMatcher = ref('filterProcessUrlRequestMatcher')
        usernameParameter = conf.apf.usernameParameter // username
        passwordParameter = conf.apf.passwordParameter // password
        continueChainBeforeSuccessfulAuthentication = conf.apf.continueChainBeforeSuccessfulAuthentication // false
        allowSessionCreation = conf.apf.allowSessionCreation // true
        postOnly = conf.apf.postOnly // true
        storeLastUsername = conf.apf.storeLastUsername // false
    }

    println 'Configuring JOOQ'

    jooqTransactionAwareDataSourceProxy(TransactionAwareDataSourceProxy) { bean ->
        bean.constructorArgs = [ref('dataSource')]
    }
    jooqDataSourceConnectionProvider(DataSourceConnectionProvider) { bean ->
        bean.constructorArgs = [ref('jooqTransactionAwareDataSourceProxy')]
    }
    jooqExceptionTranslatorExecuteListenerProvider(DefaultExecuteListenerProvider) { bean ->
        bean.constructorArgs = [new JooqExceptionTranslator()]
    }
    jooqConfiguration(DefaultConfiguration) { bean ->
        SQLDialect = org.jooq.SQLDialect.POSTGRES
        connectionProvider = ref('jooqDataSourceConnectionProvider')
    }
    dslContext(DefaultDSLContext) { bean ->
        bean.constructorArgs = [ref('jooqConfiguration')]
    }

    // CMS
    grailsUrlMappingsHolder(UrlMappingsHolderFactoryBean) {

    }

}
