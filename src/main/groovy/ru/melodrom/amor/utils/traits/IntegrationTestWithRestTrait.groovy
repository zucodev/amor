package ru.melodrom.amor.utils.traits

import grails.plugins.rest.client.RequestCustomizer
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import org.springframework.beans.factory.annotation.Value

trait IntegrationTestWithRestTrait {

    @Value('${local.server.port}')
    Integer serverPort

    String restPrefix = '/api'

    String cookiesHeader = null

    RestBuilder rest = new RestBuilder(connectTimeout: 300000, readTimeout: 300000)

    RestResponse restGet(String uri, @DelegatesTo(RequestCustomizer) Closure closure = null) {
        restGetWithPrefix(restPrefix, uri, null, closure)
    }

    RestResponse restGet(String uri, Map urlVariables, @DelegatesTo(RequestCustomizer) Closure closure = null) {
        restGetWithPrefix(restPrefix, uri, urlVariables, closure)
    }

    RestResponse restGetWithPrefix(String restPrefix,
                                   String uri,
                                   Map urlVariables = null,
                                   @DelegatesTo(RequestCustomizer) Closure closure = null) {
        String url = "http://localhost:${serverPort}${restPrefix ?: ''}/${uri}"

        Closure restClosure = getCookiesClosure()

        if (closure)
            restClosure = restClosure << closure

        println "GET ${url}"
        RestResponse response = urlVariables ? rest.get(url, urlVariables, restClosure) : rest.get(url, restClosure)
        println "${response.text}\n"
        return response
    }

    RestResponse restPost(String uri, @DelegatesTo(RequestCustomizer) Closure closure = null) {
        restPostWithPrefix(restPrefix, uri, null, closure)
    }

    RestResponse restPost(String uri, Map urlVariables, @DelegatesTo(RequestCustomizer) Closure closure = null) {
        restPostWithPrefix(restPrefix, uri, urlVariables, closure)
    }

    RestResponse restPostWithPrefix(String restPrefix,
                                    String uri,
                                    Map urlVariables = null,
                                    @DelegatesTo(RequestCustomizer) Closure closure = null) {
        String url = "http://localhost:${serverPort}${restPrefix ?: ''}/${uri}"

        Closure restClosure = getCookiesClosure()

        if (closure)
            restClosure = restClosure << closure

        println "POST ${url}"
        RestResponse response = urlVariables ? rest.post(url, urlVariables, restClosure) : rest.post(url, restClosure)
        println "${response.text}\n"
        return response
    }

    RestResponse restPut(String uri, @DelegatesTo(RequestCustomizer) Closure closure = null) {
        restPutWithPrefix(restPrefix, uri, null, closure)
    }

    RestResponse restPut(String uri, Map urlVariables, @DelegatesTo(RequestCustomizer) Closure closure = null) {
        restPutWithPrefix(restPrefix, uri, urlVariables, closure)
    }

    RestResponse restPutWithPrefix(String restPrefix,
                                   String uri,
                                   Map urlVariables = null,
                                   @DelegatesTo(RequestCustomizer) Closure closure = null) {
        String url = "http://localhost:${serverPort}${restPrefix ?: ''}/${uri}"

        Closure restClosure = getCookiesClosure()

        if (closure)
            restClosure = restClosure << closure

        println "PUT ${url}"
        RestResponse response = urlVariables ? rest.put(url, urlVariables, restClosure) : rest.put(url, restClosure)
        println "${response.text}\n"
        return response
    }

    RestResponse restPatch(String uri, @DelegatesTo(RequestCustomizer) Closure closure = null) {
        restPatchWithPrefix(restPrefix, uri, null, closure)
    }

    RestResponse restPatch(String uri, Map urlVariables, @DelegatesTo(RequestCustomizer) Closure closure = null) {
        restPatchWithPrefix(restPrefix, uri, urlVariables, closure)
    }

    RestResponse restPatchWithPrefix(String restPrefix,
                                    String uri,
                                    Map urlVariables = null,
                                    @DelegatesTo(RequestCustomizer) Closure closure = null) {
        String url = "http://localhost:${serverPort}${restPrefix ?: ''}/${uri}"

        Closure restClosure = getCookiesClosure()

        if (closure)
            restClosure = restClosure << closure

        println "PATCH ${url}"
        RestResponse response = urlVariables ? rest.patch(url, urlVariables, restClosure) : rest.patch(url, restClosure)
        println "${response.text}\n"
        return response
    }

    RestResponse restDelete(String uri, @DelegatesTo(RequestCustomizer) Closure closure = null) {
        restDeleteWithPrefix(restPrefix, uri, null, closure)
    }

    RestResponse restDelete(String uri, Map urlVariables, @DelegatesTo(RequestCustomizer) Closure closure = null) {
        restDeleteWithPrefix(restPrefix, uri, urlVariables, closure)
    }

    RestResponse restDeleteWithPrefix(String restPrefix,
                                      String uri,
                                      Map urlVariables = null,
                                      @DelegatesTo(RequestCustomizer) Closure closure = null) {
        String url = "http://localhost:${serverPort}${restPrefix ?: ''}/${uri}"

        Closure restClosure = getCookiesClosure()

        if (closure)
            restClosure = restClosure << closure

        println "DELETE ${url}"
        RestResponse response = urlVariables ? rest.delete(url, urlVariables, restClosure) : rest.delete(url, restClosure)
        println "${response.text}\n"
        return response
    }

    List<HttpCookie> getCookies(RestResponse response, String cookieName = null) {
        List<HttpCookie> result = []

        def headers = response.responseEntity.getHeaders()
        List<String> cookiesHeader = headers.get('Set-Cookie')

        cookiesHeader.each {
            List<HttpCookie> cookies = HttpCookie.parse('Set-Cookie: ' + it)

            if (cookies) {
                if (cookieName) {
                    cookies.each { cookie ->
                        if (cookie.name.trim().toLowerCase() == cookieName.trim().toLowerCase()) {
                            result.add(cookie)
                        }
                    }
                } else {
                    result.addAll(cookies)
                }
            }
        }

        return result
    }

    String getCookie(RestResponse response, String cookieName) {
        List<HttpCookie> cookies = getCookies(response, cookieName)

        if (cookies) {
            return cookies[0].value
        } else {
            return null
        }
    }

    private Closure getCookiesClosure() {
        return {
            if (cookiesHeader) {
                println "> Cookie: $cookiesHeader"
                header('Cookie', cookiesHeader)
            }
        }
    }

}
