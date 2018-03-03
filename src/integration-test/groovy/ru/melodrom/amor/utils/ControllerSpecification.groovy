package ru.melodrom.amor.utils

import com.fasterxml.jackson.databind.ObjectMapper
import grails.util.GrailsWebMockUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Specification

class ControllerSpecification<CONTROLLER> extends Specification {
    @Autowired
    CONTROLLER controller

    @Autowired
    WebApplicationContext ctx
    @Autowired
    ObjectMapper mapper

    def setup() {
        GrailsWebMockUtil.bindMockWebRequest(ctx)
    }

    def cleanup() {
        RequestContextHolder.resetRequestAttributes()
    }

    def response() {
        mapper.readValue(controller.response.content.toByteArray(), Map)
    }
    def reset() {
        controller.request.removeAllParameters()
        controller.response.setCommitted(false)
        controller.response.reset()
        controller.flash.message = ""
        controller.params.clear()
    }
}
