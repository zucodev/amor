package ru.melodrom.amor.utils

import grails.compiler.GrailsCompileStatic
import groovy.util.logging.Slf4j

import javax.websocket.*

/**
 * ChatServer Client
 *
 */
@GrailsCompileStatic
@Slf4j
@ClientEndpoint
class WebSocketClientEndpoint {

    Session userSession = null
    private MessageHandler messageHandler

    WebSocketClientEndpoint(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer()
            container.connectToServer(this, endpointURI)
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    void onOpen(Session userSession, EndpointConfig config) {
        log.debug "WebSocket open: ${this.userSession?.id}"
        this.userSession = userSession
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    void onClose(Session userSession, CloseReason reason) {
        log.debug "WebSocket close: ${this.userSession?.id} [${reason?.closeCode}:${reason?.reasonPhrase}]"
        this.userSession = null
    }

    /**
     * Callback hook for error events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnError
    void onError(Session userSession, Throwable e) {
        log.error "WebSocket error: ${this.userSession?.id}", e
        this.userSession = null
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message)
        }
    }

    /**
     * register message handler
     *
     * @param msgHandler
     */
    void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler
    }

    /**
     * Send a message.
     *
     * @param message
     */
    void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message)
    }

    /**
     * Message handler.
     *
     */
    static interface MessageHandler {

        void handleMessage(String message)
    }
}