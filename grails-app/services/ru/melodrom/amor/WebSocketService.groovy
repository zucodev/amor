package ru.melodrom.amor

import ru.melodrom.amor.utils.WebSocketClientEndpoint
import grails.compiler.GrailsCompileStatic
import groovy.util.logging.Slf4j

@GrailsCompileStatic
@Slf4j
class WebSocketService {

    final private Map<String, WebSocketClientEndpoint> clients = new HashMap<>()

    def addClient(final String address) {
        log.info "Add address for [${address}]"

        def socket = "wss://www.blockonomics.co/payment/${address}?timestamp=${(Long)(System.currentTimeMillis() / 1000L)}"

        log.info "Create client for [${socket}]"

        final WebSocketClientEndpoint clientEndPoint = new WebSocketClientEndpoint(new URI(socket));

        // add listener
        clientEndPoint.addMessageHandler(new WebSocketClientEndpoint.MessageHandler() {
            public void handleMessage(String message) {
                log.info "Recieve INFO for ${address}: ${message}"
            }
        });

        clients.put(address, clientEndPoint)

    }
}
