package com.socnet.persistence;

import com.socnet.entity.Room;
import com.socnet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomPersistence extends JpaRepository<Room, String> {

    @Query("SELECT r FROM Room r WHERE r.isDialog = true AND ?1 MEMBER OF r.users  AND  ?2 MEMBER OF r.users")
    Room findDialogBetweenUsers(User user1, User user2);

    @Query("SELECT r FROM Room r WHERE ?1 MEMBER OF r.users")
    List<Room> findRoomsByUser(User user);
}
