package com.socnet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.Message;
import com.socnet.entity.Room;
import com.socnet.entity.User;
import com.socnet.entity.dto.RoomCreateDto;
import com.socnet.service.RoomService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/room/")
public class RoomController {
    @Autowired
    private RoomService roomService;

    Logger logger = Logger.getLogger(RoomController.class);

    @JsonView(Room.RoomMessageView.class)
    @RequestMapping(method = RequestMethod.POST)
    public Room startChat(@RequestBody @Valid RoomCreateDto dto) {
        return roomService.startChatting(dto);
    }

    @JsonView(Room.RoomMessageView.class)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public List<Message> findMessages(@PathVariable("id") String roomId) {
        return roomService.findMessages(roomId);
    }

    @JsonView(Room.RoomMessageView.class)
    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public Room addUserToRoom(@PathVariable("id") String roomId,
                              @RequestBody Set<String> users) {
        return roomService.addUsersToRoom(roomId, users);
    }

    @JsonView(Room.RoomMessageView.class)
    @RequestMapping(value = "{roomId}/user/{userId}", method = RequestMethod.DELETE)
    public Room deleteUserFromRoom(@PathVariable("roomId") String roomId,
                                   @PathVariable("userId") String userId) {
        return roomService.deleteUserFromRoom(roomId, userId);
    }

    @JsonView(Room.RoomMessageView.class)
    @RequestMapping(value = "{id}/users", method = RequestMethod.GET)
    public Set<User> findUsersInTheRoom(@PathVariable("id") String roomId) {
        return roomService.findUsersByRoom(roomId);
    }

    @JsonView(Room.RoomMessageView.class)
    @RequestMapping(value = "{id}/leave", method = RequestMethod.DELETE)
    public void leaveUserFromRoom(@PathVariable("id") String roomId) {
        roomService.leaveRoom(roomId);
    }

    @JsonView(Room.RoomView.class)
    @RequestMapping(method = RequestMethod.GET)
    public List<Room> findMyRooms() {
        return roomService.findRoomsByCurrentUser();
    }

    @JsonView(Room.RoomView.class)
    @RequestMapping(value = "{id}/title", method = RequestMethod.POST)
    public Room setTitle(@PathVariable("id") String roomId, @RequestBody String title) {
        return roomService.setNewTitle(roomId, title);
    }

}
