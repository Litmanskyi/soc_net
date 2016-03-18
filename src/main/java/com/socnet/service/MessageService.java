package com.socnet.service;

import com.socnet.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> findMessagesByRoomId(String roomId);

    Message addMessageToRoom(String message, String roomId);

    void deleteMessageFromRoom(String messageId);
}
