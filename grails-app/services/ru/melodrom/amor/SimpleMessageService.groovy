package ru.melodrom.amor

import grails.compiler.GrailsCompileStatic
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j
import org.springframework.messaging.simp.SimpMessagingTemplate

import com.sun.management.OperatingSystemMXBean;
import groovy.json.JsonBuilder
import org.springframework.scheduling.annotation.Scheduled

import java.lang.management.ManagementFactory


@GrailsCompileStatic
@Slf4j
class SimpleMessageService {

    SimpMessagingTemplate brokerMessagingTemplate
    OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()

    // this will send a message to an endpoint on which a client can subscribe
    @CompileStatic(TypeCheckingMode.SKIP)
    @Scheduled(fixedRate = 5000L)
    void systemCpuLoad() {

        def cpuLoad = bean.getSystemCpuLoad()
        cpuLoad = (cpuLoad*100).round(2)

        def builder = new JsonBuilder()
        builder {
            message("CPU system load: " + cpuLoad + "%")
            timestamp(new Date())
        }
        def msg = builder.toString()
        brokerMessagingTemplate.convertAndSend "/topic/cpuLoad", msg
    }
}