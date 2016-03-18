package com.socnet.service;

import com.socnet.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> findMessagesByRoomId(String roomId);
    
    void deleteMessageFromRoom(String messageId);
}
