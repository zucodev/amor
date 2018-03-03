package ru.melodrom.amor

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import groovy.transform.CompileStatic
import org.apache.catalina.connector.Connector
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableAsync

@CompileStatic
@SpringBootApplication
@ComponentScan(['ru.melodrom.amor'])
@EnableAsync
@EnableAutoConfiguration(exclude = [JtaAutoConfiguration])
class Application extends GrailsAutoConfiguration {

    static void main(String[] args) {
        ConfigurableApplicationContext context = GrailsApp.run(Application, args)
    }

    // Set maxPostSize of embedded tomcat server to 10 megabytes (default is 2 MB, not large enough to support file uploads > 1.5 MB)
    @Bean
    EmbeddedServletContainerCustomizer containerCustomizer() throws Exception {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            void customize(ConfigurableEmbeddedServletContainer container) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container
                tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer() {
                    @Override
                    void customize(Connector connector) {
                        connector.setMaxPostSize(10000000)
                    }
                })
            }
        }
    }

}