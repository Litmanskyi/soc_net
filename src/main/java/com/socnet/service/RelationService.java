package com.socnet.service;

import com.socnet.entity.enumaration.RelationStatus;
import com.socnet.entity.Relation;
import com.socnet.entity.User;

import javax.transaction.Transactional;
import java.util.List;

public interface RelationService {
    Relation addFriend(String targetId);

    Relation addToBlacklist(String targetId);

    void deleteFriend(String targetId);

    void deleteFromBlacklist(String targetId);

    List<User> findFriends(String user_id);

    List<User> findBlacklist(String user_id);

    List<User> findFollowers(String userId);

    Relation findRelation(User sender, User receiver);

    boolean isFriends(User user1, User user2);

    boolean isSomeoneInBlacklist(User user1, User user2);

    boolean isSomeoneInBlacklist(User user1, List<User> users);
}
