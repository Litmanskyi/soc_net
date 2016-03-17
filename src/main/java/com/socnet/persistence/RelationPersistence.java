package com.socnet.persistence;

import com.socnet.entity.Relation;
import com.socnet.entity.enumaration.RelationStatus;
import com.socnet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationPersistence extends JpaRepository<Relation, String> {

    @Query("SELECT f FROM Relation f WHERE f.sender.id=?1 AND f.receiver.id=?2")
    Relation findRelation(String senderId, String receiverId);

    @Query("select f.receiver FROM Relation f WHERE f.sender.id=?1 AND f.status=?2")
    List<User> findReceiversBySenderIdAndStatus(String user_id, RelationStatus friend);

    @Query("SELECT f.sender FROM Relation f WHERE f.receiver.id=?1 AND f.status=?2")
    List<User> findSendersByReceiverIdAndStatus(String user_id, RelationStatus friend);
}
