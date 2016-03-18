package com.socnet.aspect;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socnet.entity.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WebSocketAspect {

    @Autowired
    private SimpMessagingTemplate simp;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* com.socnet.service.impl.RoomServiceImpl.addMessageToRoom(..))" +
            "|| execution(* com.socnet.service.impl.RoomServiceImpl.startChatting(..))")
    public void newMessage() {
    }

    @AfterReturning(pointcut = "newMessage()", returning = "result")
    public void afterAdvice(JoinPoint joinPoint, Object result) {
        if (result instanceof Message) {
            Message message = (Message) result;
            message.getRoom().getUsers().forEach(user -> {
                String json= null;
                try {
                    objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
                    json = objectMapper.writerWithView(Message.MessageView.class).writeValueAsString(message);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                simp.convertAndSendToUser(user.getEmail(), "/queue/message", json);
            });
        }
        if (result instanceof Room) {

        }
    }
}
