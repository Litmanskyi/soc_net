package com.socnet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.Message;
import com.socnet.service.MessageService;
import com.socnet.service.RoomService;
import com.socnet.validation.validators.MessageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/room/{roomId}/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageValidator messageValidator;

    @Autowired
    private RoomService roomService;

    @InitBinder
    protected void initBinder(WebDataBinder binder)
            throws Exception {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.setValidator(messageValidator);
    }

    @JsonView(Message.MessageView.class)
    @RequestMapping(method = RequestMethod.GET)
    public List<Message> findMessages(@PathVariable("roomId") String roomId) {
        return messageService.findMessagesByRoomId(roomId);
    }

    @JsonView(Message.MessageView.class)
    @RequestMapping(method = RequestMethod.POST) //todo +++ change params String message, -- check length below
    public Message addMessageToRoom(@PathVariable("roomId") String roomId, @RequestBody @Valid String message) {
        return messageService.addMessageToRoom(roomId, message);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public void deleteMessageFromRoom(@PathVariable("messageId") String messageId) {
        messageService.deleteMessageFromRoom(messageId);
    }

}
