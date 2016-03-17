package com.socnet.service.impl;

import com.socnet.entity.Message;
import com.socnet.entity.Room;
import com.socnet.entity.User;
import com.socnet.persistence.MessagePersistence;
import com.socnet.service.MessageService;
import com.socnet.service.RoomService;
import com.socnet.utility.AuthenticatedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    public static final String ACCESS_DENIED_RIGHTS = "User doesn't have rights!";
    public static final String ROOM_NOT_FOUND = "Room not found!";

    @Autowired
    private MessagePersistence messagePersistence;

    @Autowired
    private RoomService roomService;

    @Override
    public List<Message> findMessagesByRoomId(String roomId) {
        User user = AuthenticatedUtils.getCurrentAuthUser();
        Room room = roomService.findRoom(roomId);
        if (room == null) {
            throw new EntityNotFoundException(ROOM_NOT_FOUND);
        }
        if (!room.getUsers().contains(user)) {
            throw new AccessDeniedException(ACCESS_DENIED_RIGHTS);
        }
        return room.getMessages();
    }

    @Override
    public Message addMessageToRoom(String roomId, Message mes) {
        User user = AuthenticatedUtils.getCurrentAuthUser();
        Room room = roomService.findRoom(roomId);
        if (!room.getUsers().contains(user)) { //todo to perm serv
            throw new AccessDeniedException(ACCESS_DENIED_RIGHTS);
        }
        Message message = new Message();
        message.setCreator(user);
        message.setRoom(room);
        message.setMessage(mes.getMessage());
        room.getMessages().add(message);
        return messagePersistence.save(message);
    }


    @Override
    public void deleteMessageFromRoom(String messageId) {

        User user = AuthenticatedUtils.getCurrentAuthUser();

        Message message = messagePersistence.findOne(messageId);
        Room room = message.getRoom();

        if (!room.getUsers().contains(user)) {
            throw new AccessDeniedException(ACCESS_DENIED_RIGHTS);
        }
        if (!message.getCreator().equals(user)) {
            throw new AccessDeniedException(ACCESS_DENIED_RIGHTS);
        }

        messagePersistence.delete(message);
    }
}
