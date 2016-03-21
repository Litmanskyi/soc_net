package com.socnet.controller;

import com.socnet.entity.Message;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.security.Principal;

@Controller
public class WebSocketController {

    private Logger logger = Logger.getLogger(WebSocketController.class);

    @Autowired
    private SimpMessagingTemplate simp;

    @MessageMapping("user.{userId}.message")
    public void listenerMessages(Message message, @DestinationVariable(value = "userId")
    String userId, Principal principal, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        logger.info("user id: " + userId + ", message: " + message.getMessage());
//        simp.convertAndSendToUser(principal.getName(), "/queue/message", Collections.singletonMap(SimpMessageHeaderAccessor.SESSION_ID_HEADER, headerAccessor.getSessionId()));

    }
}
