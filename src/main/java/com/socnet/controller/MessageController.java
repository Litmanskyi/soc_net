package com.socnet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.Message;
import com.socnet.service.MessageService;
import com.socnet.validation.validators.MessageValidator;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/room/{roomId}/message/")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @InitBinder
    protected void initBinder(WebDataBinder binder)
            throws Exception {
        binder.setValidator(new MessageValidator());
    }

    @JsonView(Message.MessageView.class)
    @RequestMapping(method = RequestMethod.GET)
    public List<Message> findMessages(@PathVariable("roomId") String roomId) {
        return messageService.findMessagesByRoomId(roomId);
    }

    @JsonView(Message.MessageView.class)
    @RequestMapping(method = RequestMethod.POST)
    public Message addMessageToRoom(@PathVariable("roomId") String roomId,
                                    @RequestBody @Valid Message message) {//todo change params String message, check length below
 //       return messageService.addMessageToRoom(roomId, message);
        return null;
    }

    @RequestMapping(value = "{messageId}", method = RequestMethod.DELETE)
    public void deleteMessageFromRoom(@PathVariable("messageId") String messageId) {
        messageService.deleteMessageFromRoom(messageId);
    }

}
