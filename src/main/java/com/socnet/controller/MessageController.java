package com.socnet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.Message;
import com.socnet.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/room/{roomId}/message/")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @JsonView(Message.MessageView.class)
    @RequestMapping(method = RequestMethod.GET)
    public List<Message> findMessages(@PathVariable("roomId") String roomId) {
        return messageService.findMessagesByRoomId(roomId);
    }

    @JsonView(Message.MessageView.class)
    @RequestMapping(method = RequestMethod.POST)
    public Message addMessageToRoom(@PathVariable("roomId") String roomId, @RequestBody @Valid Message message) {//todo change params String message, check length below
        return messageService.addMessageToRoom(roomId, message);
    }

    @RequestMapping(value = "{messageId}", method = RequestMethod.DELETE)
    public void deleteMessageFromRoom(@PathVariable("messageId") String messageId) {
        messageService.deleteMessageFromRoom(messageId);
    }

}
