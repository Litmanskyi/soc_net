package com.socnet.service.impl;

import com.socnet.entity.Relation;
import com.socnet.entity.User;
import com.socnet.entity.enumaration.RelationStatus;
import com.socnet.persistence.RelationPersistence;
import com.socnet.service.RelationService;
import com.socnet.service.UserService;
import com.socnet.utility.AuthenticatedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class RelationServiceImpl implements RelationService {

    private final String YOU_ARE_IN_BLACKLIST = "You are in blacklist";
    private final String USER_NOT_FOUND = "User not found!";
    private final String RELATION_NOT_FOUND = "Relation not found!";
    private final String NOT_FOUND_IN_BLACKLIST = "The user not found int blacklist!";
    private final String THE_SAME_USER = "The user can't added yourself!";
    private final String REPEATED_REQUEST = "Request has made before!";

    @Autowired
    private UserService userService;

    @Autowired
    private RelationPersistence relationPersistence;

    @Transactional
    @Override
    public Relation addFriend(String targetId) {
        User sender = AuthenticatedUtils.getCurrentAuthUser();
        User receiver = userService.findUserById(targetId);

        checkIfExistAndNotSameUser(sender, receiver);

        Relation relationReceiver = relationPersistence.findRelation(targetId, sender.getId());
        if (relationReceiver != null && isStatusEqual(relationReceiver, RelationStatus.BLACK)) {
            throw new IllegalArgumentException(YOU_ARE_IN_BLACKLIST);
        }

        Relation relationSender = relationPersistence.findRelation(sender.getId(), targetId);
        if (relationSender == null) {
            relationSender = new Relation(sender, receiver);
            relationSender.setStatus(RelationStatus.FOLLOW);
        } else if (isStatusEqual(relationSender, RelationStatus.FOLLOW)
                || isStatusEqual(relationSender, RelationStatus.FRIEND)) {
            throw new IllegalArgumentException(REPEATED_REQUEST);
        } else if (isStatusEqual(relationSender, RelationStatus.BLACK)) {
            relationSender.setStatus(RelationStatus.FOLLOW);
        }

        if (relationReceiver != null && isStatusEqual(relationReceiver, RelationStatus.FOLLOW)) {
            relationSender.setStatus(RelationStatus.FRIEND);
            relationReceiver.setStatus(RelationStatus.FRIEND);
            relationPersistence.save(relationReceiver);
        }

        return relationPersistence.save(relationSender);
    }

    @Transactional
    @Override
    public Relation addToBlacklist(String targetId) {
        User sender = AuthenticatedUtils.getCurrentAuthUser();
        User receiver = userService.findUserById(targetId);

        checkIfExistAndNotSameUser(sender, receiver);

        Relation relationSender = relationPersistence.findRelation(sender.getId(), targetId);
        if (relationSender != null &&
                isStatusEqual(relationSender, RelationStatus.BLACK)) {
            throw new IllegalArgumentException(REPEATED_REQUEST);
        }
        if (relationSender == null) {
            relationSender = new Relation(sender, receiver);
        }

        relationSender.setStatus(RelationStatus.BLACK);
        Relation relationReceiver = relationPersistence.findRelation(targetId, sender.getId());

        if (relationReceiver != null
                && !isStatusEqual(relationReceiver, RelationStatus.BLACK)) {
            relationPersistence.delete(relationReceiver);
        }

        return relationPersistence.save(relationSender);
    }

    @Transactional
    @Override
    public void deleteFriend(String targetId) {
        User sender = AuthenticatedUtils.getCurrentAuthUser();
        User receiver = userService.findUserById(targetId);

        checkIfExistAndNotSameUser(sender, receiver);

        Relation relation = relationPersistence.findRelation(sender.getId(), targetId);
        if (relation == null || !isStatusEqual(relation, RelationStatus.FRIEND)) {
            throw new EntityNotFoundException(RELATION_NOT_FOUND);
        }

        relationPersistence.delete(relation);

        relation = relationPersistence.findRelation(targetId, sender.getId());
        if (relation != null) {
            relation.setStatus(RelationStatus.FOLLOW);
            relationPersistence.save(relation);
        }
    }

    @Transactional
    @Override
    public void deleteFromBlacklist(String targetId) {
        User sender = AuthenticatedUtils.getCurrentAuthUser();
        User receiver = userService.findUserById(targetId);

        checkIfExistAndNotSameUser(sender, receiver);

        Relation relation = relationPersistence.findRelation(sender.getId(), targetId);
        if (relation == null || !isStatusEqual(relation, RelationStatus.BLACK)) {
            throw new EntityNotFoundException(NOT_FOUND_IN_BLACKLIST);
        }

        relationPersistence.delete(relation);
    }

    @Override
    public List<User> findFriends(String userId) {
        return relationPersistence.findReceiversBySenderIdAndStatus(userId, RelationStatus.FRIEND);
    }

    @Override
    public List<User> findBlacklist(String userId) {
        return relationPersistence.findReceiversBySenderIdAndStatus(userId, RelationStatus.BLACK);
    }

    @Override
    public List<User> findFollowers(String userId) {
        return relationPersistence.findSendersByReceiverIdAndStatus(userId, RelationStatus.FOLLOW);
    }

    @Override
    public Relation findRelation(User sender, User receiver) {
        return relationPersistence.findRelation(sender.getId(), receiver.getId());
    }

    private void checkIfExistAndNotSameUser(User sender, User receiver) {
        if (sender == null) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        if (receiver == null) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException(THE_SAME_USER);
        }
    }

    public boolean isStatusEqual(Relation relation, RelationStatus status) {
        if (relation.getStatus() == status) {
            return true;
        } else
            return false;
    }

    @Override
    public boolean isFriends(User user1, User user2) {
        Relation relationUser1 = findRelation(user1, user2);
        return relationUser1 != null && relationUser1.getStatus() == RelationStatus.FRIEND;
    }

    @Override
    public boolean isSomeoneInBlacklist(User user1, User user2){
        Relation relationUser1 = findRelation(user1, user2);
        Relation relationUser2 = findRelation(user1, user2);
        if ((relationUser1 != null && relationUser1.getStatus() == RelationStatus.BLACK)
                ||(relationUser2 != null && relationUser2.getStatus() == RelationStatus.BLACK)){
            return true;
        }
        return false;
    }
}
