package com.socnet.service;

import com.socnet.entity.Message;
import com.socnet.entity.Room;
import com.socnet.entity.User;
import com.socnet.entity.dto.RoomCreateDto;

import java.util.List;
import java.util.Set;

public interface RoomService {

    Room createRoom();

    Room startChatting(RoomCreateDto roomDto);

    Room addMessageToRoom(String roomId, String mes);

    Room addUsersToRoom(String roomId, Set<String> usersIds);

    Room findRoom(String roomId);

    List<Message> findMessages(String roomId);

    Room findDialog(User user1, User user2);

    Room deleteUserFromRoom(String roomId, String userId);

    Set<User> findUsersByRoom(String roomId);

    void leaveRoom(String roomId);

    List<Room> findRoomsByCurrentUser();

    Room setNewTitle(String roomId, String title);
}
