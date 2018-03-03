package ru.melodrom.amor.utils

import grails.util.Environment
import grails.util.Holders
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.grails.web.util.WebUtils

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CompileStatic
class GrailsHelper {

    static List<String> ipHeaders = ['cf-connecting-ip',
                                     'X-Forwarded-For',
                                     'clientip',
                                     'Client-IP',
                                     'X-Real-IP',
                                     'Proxy-Client-IP',
                                     'WL-Proxy-Client-IP',
                                     'rlnclientipaddr']

    static final String HEADER_PRAGMA = 'Pragma'
    static final String HEADER_EXPIRES = 'Expires'
    static final String HEADER_CACHE_CONTROL = 'Cache-Control'

    static String getAppName() {
        Holders.grailsApplication.getMetadata().getApplicationName()
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    static String getServerUrl() {
        Holders.config?.grails?.serverURL?.toString()
    }

    static String getIpAddress(HttpServletRequest request = getCurrentRequest()) {
        String ipAddress = null

        ipHeaders.each { header ->
            if (!ipAddress) {
                ipAddress = request.getHeader(header)?.trim()

                if (ipAddress?.contains(','))
                    ipAddress = ipAddress.split(',')[0]
            }
        }

        if (!ipAddress)
            ipAddress = request.remoteAddr

        if (!ipAddress)
            ipAddress = '127.0.0.1'

        return ipAddress
    }

    static HttpServletRequest getCurrentRequest() {
        try {
            return WebUtils.retrieveGrailsWebRequest()?.currentRequest
        } catch (e) {
        }
    }

    static HttpServletResponse getCurrentResponse() {
        try {
            return WebUtils.retrieveGrailsWebRequest()?.currentResponse
        } catch (e) {
        }
    }

    static void setNoCache(HttpServletResponse response = getCurrentResponse()) {
        assert response

        response.setHeader(HEADER_PRAGMA, 'no-cache')
        response.setDateHeader(HEADER_EXPIRES, 1L)
        response.setHeader(HEADER_CACHE_CONTROL, 'private')
        response.addHeader(HEADER_CACHE_CONTROL, 'no-store')
        response.addHeader(HEADER_CACHE_CONTROL, 'must-revalidate')
    }

    static String getEnvPrefix(Environment environment = Environment.current) {
        environment.name.toLowerCase()
                .replace('production', 'prod')
                .replace('development', 'dev') + '-'
    }

}
