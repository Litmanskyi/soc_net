package com.socnet.service.impl;

import com.socnet.entity.Message;
import com.socnet.entity.Room;
import com.socnet.entity.User;
import com.socnet.entity.dto.RoomCreateDto;
import com.socnet.persistence.RoomPersistence;
import com.socnet.service.MessageService;
import com.socnet.service.RelationService;
import com.socnet.service.RoomService;
import com.socnet.service.UserService;
import com.socnet.utility.AuthenticatedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoomServiceImpl implements RoomService {
    public static final String USER_NOT_FOUND = "User not found!";
    public static final String USER_IN_BLACKLIST = "User is in blacklist or you are in blacklist!";
    public static final String YOU_DON_T_BELONG_THIS_ROOM = "You don't belong this room!";
    public static final String THIS_IS_DIALOG = "This is dialog!";
    public static final String REPEATED_REQUEST = "User is already in this chat!";
    public static final String ROOM_NOT_FOUND = "Room not found!";
    public static final String ADD_YOURSELF_TO_CHAT = "You can't add yourself to chat!";
    public static final String YOU_DON_T_HAVE_RIGHTS_TO_DELETE = "You don't have rights to delete!";
    public static final String DELETE_USER_FROM_DIALOG = "You can't delete user from dialog";

    @Autowired
    private UserService userService;

    @Autowired
    private RoomPersistence roomPersistence;

    @Autowired
    private RelationService relationService;

    @Autowired
    private MessageService messageService;

    @Transactional
    @Override
    public Room createRoom() {
        User currentUser = AuthenticatedUtils.getCurrentAuthUser();
        Room room = new Room();
        room.setAdmin(currentUser);
        room.getUsers().add(currentUser);
        return roomPersistence.save(room);
    }

    //todo refactor
    @Override
    public Room startChatting(RoomCreateDto roomDto) {
        Set<User> receivers = new HashSet<>();
        User currentUser = AuthenticatedUtils.getCurrentAuthUser();
        roomDto.getUsersId().forEach(receiverId ->{
            User receiver = userService.findUserById(receiverId);
            if (receiver == null) {
                throw new EntityNotFoundException(USER_NOT_FOUND);
            }
            if (relationService.isSomeoneInBlacklist(currentUser, receiver)) {
                throw new IllegalArgumentException(USER_IN_BLACKLIST);
            }
            receivers.add(receiver);
        });

        if (receivers.size() < 1) {
            throw new IllegalArgumentException("You can't create chat without user(s)!");
        }

        Room room = findDialog(currentUser, receivers.iterator().next());
        if (receivers.size() == 1 && room != null) {
            Message message = new Message(currentUser, room, roomDto.getMessage());
            room.getMessages().add(message);
            roomPersistence.flush();
            return roomPersistence.save(room);
        }

        room = createRoom();
        if (receivers.size() > 1) {
            room.setTitle(roomDto.getTitle());
            room.setDialog(false);
        } else {
            room.setDialog(true);
        }

        room.getUsers().addAll(receivers);
        room.getMessages().add(new Message(currentUser, room, roomDto.getMessage()));
        return roomPersistence.save(room);
    }

    @Override
    public Room addUsersToRoom(String roomId, Set<String> usersIds) {
        Room room = findRoom(roomId);

        if (room == null) {
            throw new EntityNotFoundException(ROOM_NOT_FOUND);
        }

        User currentUser = AuthenticatedUtils.getCurrentAuthUser();
        Set<User> users = room.getUsers();

        usersIds.forEach(userId -> {
            User user = userService.findUserById(userId);//todo use findAll
            checkAddUserToRoom(room, user, currentUser);
            users.add(user);
        });

        room.setUsers(users);
        return roomPersistence.save(room);
    }

    @Override
    public Room findRoom(String roomId) {
        return roomPersistence.findOne(roomId);
    }

    @Override
    public List<Message> findMessages(String roomId) {
        User user = AuthenticatedUtils.getCurrentAuthUser();
        Room room = findRoom(roomId);
        if (room == null) {
            throw new IllegalArgumentException(ROOM_NOT_FOUND);
        }
        if (!room.getUsers().contains(user)) {
            throw new AccessDeniedException(YOU_DON_T_BELONG_THIS_ROOM);
        }
        return room.getMessages();
    }

    @Override
    public Room findDialog(User user1, User user2) {
        return roomPersistence.findDialogBetweenUsers(user1, user2);
    }

    @Override
    public Room deleteUserFromRoom(String roomId, String userId) {
        User uToRemove = userService.findUserById(userId);
        Room room = findRoom(roomId);
        User currUser = AuthenticatedUtils.getCurrentAuthUser();

        checkUserAndRoom(uToRemove, room);
        if (room.isDialog()) {
            throw new IllegalArgumentException(DELETE_USER_FROM_DIALOG);
        }
        if (!room.getAdmin().equals(currUser)) {
            throw new AccessDeniedException(YOU_DON_T_HAVE_RIGHTS_TO_DELETE);
        }

        room.getUsers().remove(uToRemove);
        return roomPersistence.save(room);
    }

    @Override
    public Set<User> findUsersByRoom(String roomId) {
        Room room = findRoom(roomId);
        User currUser = AuthenticatedUtils.getCurrentAuthUser();

        checkUserAndRoom(currUser, room);

        return room.getUsers();
    }

    //todo unite leave and delete from room
    @Override
    public void leaveRoom(String roomId) {
        Room room = findRoom(roomId);
        User currUser = AuthenticatedUtils.getCurrentAuthUser();

        checkUserAndRoom(currUser, room);

        room.getUsers().remove(currUser);
        if (room.getUsers().size() == 0) {
            roomPersistence.delete(room.getId());
        } else {
            roomPersistence.save(room);
        }
    }

    @Override
    public List<Room> findRoomsByCurrentUser() {
        User currUser = AuthenticatedUtils.getCurrentAuthUser();
        return roomPersistence.findRoomsByUser(currUser);
    }

    @Override
    public Room setNewTitle(String roomId, String title) {
        Room room = roomPersistence.findOne(roomId);
        User currUser = AuthenticatedUtils.getCurrentAuthUser();

        checkUserAndRoom(currUser, room);
        if (!room.isDialog()) {
            throw new AccessDeniedException(THIS_IS_DIALOG);
        }

        room.setTitle(title);
        return room;
    }

    // todo permissionService
    private void checkAddUserToRoom(Room room, User nUser, User currUser) {
        if (nUser == null) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        if (!room.getUsers().contains(currUser)) {
            throw new AccessDeniedException(YOU_DON_T_BELONG_THIS_ROOM);
        }
        if (room.isDialog()) {
            throw new AccessDeniedException(THIS_IS_DIALOG);
        }
        if (nUser.equals(currUser)) {
            throw new IllegalArgumentException(ADD_YOURSELF_TO_CHAT);
        }
        if (room.getUsers().contains(nUser)) {
            throw new IllegalArgumentException(REPEATED_REQUEST);
        }
        if (relationService.isSomeoneInBlacklist(currUser, nUser)) {
            throw new IllegalArgumentException(USER_IN_BLACKLIST);
        }
    }

    private void checkUserAndRoom(User user, Room room) {
        if (user == null) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        if (room == null) {
            throw new EntityNotFoundException(ROOM_NOT_FOUND);
        }
        if (!room.getUsers().contains(user)) {
            throw new AccessDeniedException(YOU_DON_T_BELONG_THIS_ROOM);
        }

    }
}
