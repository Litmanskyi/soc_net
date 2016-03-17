package com.socnet.service;

import com.socnet.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> findMessagesByRoomId(String roomId);

    Message addMessageToRoom(String roomId, String mes);

    void deleteMessageFromRoom(String messageId);
}
