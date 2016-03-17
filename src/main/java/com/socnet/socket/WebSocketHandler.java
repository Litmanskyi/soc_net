package com.socnet.socket;

import org.apache.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketHandler {
    private Logger logger = Logger.getLogger(WebSocketHandler.class);

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        logger.info("Connected");
        logger.info(event.getUser().getName());

    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        logger.info("DISCONNECTED");
//        logger.info(event.getUser().getName());
    }
}
