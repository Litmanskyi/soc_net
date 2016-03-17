package com.socnet.service.impl;

import com.socnet.entity.Relation;
import com.socnet.entity.User;
import com.socnet.entity.enumaration.RelationStatus;
import com.socnet.persistence.RelationPersistence;
import com.socnet.service.UserService;
import com.socnet.utility.AuthenticatedUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthenticatedUtils.class)
public class RelationServiceImplTest {
    private static final String USER_ID_RCVR = "1";
    private static final String USER_ID_SNDR = "2";
    private static final User sender = getUserWithId(USER_ID_SNDR);
    private static final User receiver = getUserWithId(USER_ID_RCVR);

    @Mock
    private UserService userService;

    @Mock
    private RelationPersistence friendPersistence;

    @Mock
    private AuthenticatedUtils authenticatedUtils;

    @InjectMocks
    private RelationServiceImpl relationService;

    private static User getUserWithId(String id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @Before
    public void setUp() {
        PowerMockito.mockStatic(AuthenticatedUtils.class);
        BDDMockito.given(AuthenticatedUtils.getCurrentAuthUser()).willReturn(sender);

        Mockito.when(userService.findUserById(USER_ID_SNDR)).thenReturn(sender);
        Mockito.when(userService.findUserById(USER_ID_RCVR)).thenReturn(receiver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRepeatedRequestAddToFriend() throws Exception {
        Relation relationSender = getRelationBetweenUsersAndStatusEqual(sender, receiver, RelationStatus.FOLLOW);

        Mockito.when(friendPersistence.findRelation(USER_ID_SNDR, USER_ID_RCVR)).thenReturn(relationSender);

        relationService.addFriend(USER_ID_RCVR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToFriendWhenSenderInBlackList() {
        Relation relationReceiver = getRelationBetweenUsersAndStatusEqual(receiver, sender, RelationStatus.BLACK);

        Mockito.when(friendPersistence.findRelation(USER_ID_RCVR, USER_ID_SNDR)).thenReturn(relationReceiver);

        relationService.addFriend(USER_ID_RCVR);
    }
    @Test
    public void testAddToFriendWhenReceiverWasInBlackList() {
        Relation relationSender = getRelationBetweenUsersAndStatusEqual(sender, receiver, RelationStatus.BLACK);

        Mockito.when(friendPersistence.findRelation(USER_ID_SNDR, USER_ID_RCVR)).thenReturn(relationSender);

        relationService.addFriend(USER_ID_RCVR);
    }
    @Test
    public void testAddToFriendWhenUserReceiverFollowed() {
        Relation relationSender = getRelationBetweenUsersAndStatusEqual(sender, receiver, RelationStatus.FRIEND);
        Relation relationReceiverFollow = getRelationBetweenUsersAndStatusEqual(receiver, sender, RelationStatus.FOLLOW);
        Relation relationReceiverFriend = getRelationBetweenUsersAndStatusEqual(receiver, sender, RelationStatus.FRIEND);

        Mockito.when(friendPersistence.findRelation(USER_ID_RCVR, USER_ID_SNDR)).thenReturn(relationReceiverFollow);
        Mockito.when(friendPersistence.save(relationReceiverFriend)).thenReturn(relationReceiverFriend);
        Mockito.when(friendPersistence.save(relationSender)).thenReturn(relationSender);

        relationSender = relationService.addFriend(USER_ID_RCVR);

        Mockito.verify(friendPersistence, Mockito.times(1)).findRelation(USER_ID_RCVR, USER_ID_SNDR);
        Mockito.verify(friendPersistence, Mockito.times(1)).findRelation(USER_ID_SNDR, USER_ID_RCVR);
        Mockito.verify(friendPersistence, Mockito.times(2)).save(relationReceiverFollow);

        Assert.assertEquals(relationSender.getStatus(), RelationStatus.FRIEND);
        Assert.assertEquals(relationSender.getSender().getId(), USER_ID_SNDR);
        Assert.assertEquals(relationSender.getReceiver().getId(), USER_ID_RCVR);
    }

    @Test
    public void testAddToFriendWhenFriendReceiverNotExisted() {
        Relation relationSender = getRelationBetweenUsersAndStatusEqual(sender, receiver, RelationStatus.FOLLOW);

        Mockito.when(friendPersistence.save(relationSender)).thenReturn(relationSender);

        relationSender = relationService.addFriend(USER_ID_RCVR);

        Mockito.verify(friendPersistence, Mockito.times(1)).save(relationSender);

        Assert.assertEquals(relationSender.getStatus(), RelationStatus.FOLLOW);
        Assert.assertEquals(relationSender.getSender().getId(), USER_ID_SNDR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToBlackListTheSameUser() {
        relationService.addToBlacklist(USER_ID_SNDR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRepeatedRequestToAddToBlackList() {
        Relation relationSender = getRelationBetweenUsersAndStatusEqual(sender, receiver, RelationStatus.BLACK);

        Mockito.when(friendPersistence.findRelation(USER_ID_SNDR, USER_ID_RCVR)).thenReturn(relationSender);
        relationService.addToBlacklist(USER_ID_RCVR);
    }

    @Test
    public void testAddToBlacklistWhenReceiverWithStatusNotNullAndNotEqualBlack() {
        Relation relationSender = getRelationBetweenUsersAndStatusEqual(sender, receiver, RelationStatus.BLACK);
        Relation relationReceiver = getRelationBetweenUsersAndStatusEqual(receiver, sender, RelationStatus.FOLLOW);

        Mockito.when(friendPersistence.findRelation(USER_ID_SNDR, USER_ID_RCVR)).thenReturn(null);
        Mockito.when(friendPersistence.findRelation(USER_ID_RCVR, USER_ID_SNDR)).thenReturn(relationReceiver);
        Mockito.when(friendPersistence.save(relationSender)).thenReturn(relationSender);

        relationSender = relationService.addToBlacklist(USER_ID_RCVR);

        Mockito.verify(friendPersistence, Mockito.times(1)).save(relationSender);
        Mockito.verify(friendPersistence, Mockito.times(1)).findRelation(USER_ID_SNDR, USER_ID_RCVR);
        Mockito.verify(friendPersistence, Mockito.times(1)).findRelation(USER_ID_RCVR, USER_ID_SNDR);
        Assert.assertEquals(relationSender.getSender().getId(), USER_ID_SNDR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteFriendTheSameUser() {


        relationService.deleteFriend(USER_ID_SNDR);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteFriendWhenStatusUsersNotFriend() {
        Relation relationSender = getRelationBetweenUsersAndStatusEqual(sender, receiver, RelationStatus.FOLLOW);

        Mockito.when(friendPersistence.findRelation(USER_ID_SNDR, USER_ID_RCVR)).thenReturn(relationSender);
        relationService.deleteFriend(USER_ID_RCVR);
    }

    @Test
    public void testDeleteFriendWithExistedFriends() {
        Relation relationSender = getRelationBetweenUsersAndStatusEqual(sender, receiver, RelationStatus.FRIEND);
        Relation relationReceiver = getRelationBetweenUsersAndStatusEqual(receiver, sender, RelationStatus.FOLLOW);

        Mockito.when(friendPersistence.findRelation(USER_ID_SNDR, USER_ID_RCVR)).thenReturn(relationSender);
        Mockito.when(friendPersistence.findRelation(USER_ID_RCVR, USER_ID_SNDR)).thenReturn(relationReceiver);
        Mockito.when(friendPersistence.save(relationReceiver)).thenReturn(relationReceiver);

        relationService.deleteFriend(USER_ID_RCVR);

        Mockito.verify(friendPersistence, Mockito.times(1)).save(relationReceiver);
        Mockito.verify(friendPersistence, Mockito.times(1)).delete(relationSender);
        Mockito.verify(friendPersistence, Mockito.times(1)).findRelation(USER_ID_SNDR, USER_ID_RCVR);
        Mockito.verify(friendPersistence, Mockito.times(1)).findRelation(USER_ID_RCVR, USER_ID_SNDR);
        Mockito.verify(userService, Mockito.times(1)).findUserById(USER_ID_RCVR);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteFromBlackListIfUserNotExisted() {
        Mockito.when(friendPersistence.findRelation(USER_ID_SNDR, USER_ID_RCVR)).thenReturn(null);

        relationService.deleteFromBlacklist(USER_ID_RCVR);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteFromBlackListWhenStatusNotEqualBlack() {
        Relation relationSender = getRelationBetweenUsersAndStatusEqual(sender, receiver, RelationStatus.FOLLOW);

        Mockito.when(friendPersistence.findRelation(USER_ID_SNDR, USER_ID_RCVR)).thenReturn(relationSender);

        relationService.deleteFromBlacklist(USER_ID_RCVR);
    }

    @Test
    public void testDeleteFromBlackListWithExistedFriend() {
        Relation relationSender = getRelationBetweenUsersAndStatusEqual(sender, receiver, RelationStatus.BLACK);

        Mockito.when(friendPersistence.findRelation(USER_ID_SNDR, USER_ID_RCVR)).thenReturn(relationSender);

        relationService.deleteFromBlacklist(USER_ID_RCVR);
    }

    private Relation getRelationBetweenUsersAndStatusEqual(User sender, User receiver, RelationStatus status) {
        Relation relation = new Relation(sender, receiver);
        relation.setStatus(status);
        return relation;
    }

}