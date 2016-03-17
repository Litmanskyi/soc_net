package com.socnet.service.impl;

import com.socnet.entity.User;
import com.socnet.persistence.UserPersistence;
import com.socnet.service.WallService;
import com.socnet.utility.AuthenticatedUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthenticatedUtils.class)
public class UserServiceImplTest {

    private static final String USER_ID = "1";
    private static final String USER_EMAIL = "email";
    private static final String USER_FIRSTNAME = "firstname";
    private static final String USER_PASSWORD = "password";
    private static User user;
    @Mock
    private UserPersistence userPersistence;

    @Mock
    private WallService wallService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Before
    public void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setFirstName(USER_FIRSTNAME);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);

        PowerMockito.mockStatic(AuthenticatedUtils.class);
        BDDMockito.given(AuthenticatedUtils.getCurrentAuthUser()).willReturn(user);

        Mockito.when(userPersistence.findOne(USER_ID)).thenReturn(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserWithExistedId() {
        user = userServiceImpl.createUser(user);

    }

    @Test
    public void testCreateUserWithoutUserId() {
        User user = new User();
        user.setEmail(USER_EMAIL);

        Mockito.when(userPersistence.save(user)).thenReturn(this.user);

        user = userServiceImpl.createUser(user);

        Assert.assertEquals(USER_ID, user.getId());
        Assert.assertEquals(USER_EMAIL, user.getEmail());
    }

    @Test
    public void testUpdateUserWithExistedId() {

        Mockito.when(userPersistence.save(user)).thenReturn(user);

        user = userServiceImpl.updateUser(user);

        Mockito.verify(userPersistence, Mockito.times(1)).save(user);

        Assert.assertEquals(user.getId(), USER_ID);
        Assert.assertEquals(user.getEmail(),USER_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserWithAnotherUserIdParameter() {
        User user = new User();
        user.setId("5");

        userServiceImpl.updateUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUseWithoutEmail() {
        User user = new User();

        Mockito.when(userPersistence.save(user)).thenReturn(null);

        userServiceImpl.createUser(user);

    }

    @Test
    public void testGetUserByEmailExistedUser() {
        Mockito.when(userPersistence.findUserByEmail(USER_EMAIL)).thenReturn(user);
        String result = userServiceImpl.findUserByEmail(USER_EMAIL).getId();
        Mockito.verify(userPersistence, Mockito.times(1)).findUserByEmail(USER_EMAIL);

        Assert.assertEquals(USER_ID, result);
    }

    @Test
    public void testGetUserByEmailNonExistedUser() {
        Mockito.when(userPersistence.findUserByEmail(USER_EMAIL)).thenReturn(null);
        User result=userServiceImpl.findUserByEmail(USER_EMAIL);
        Assert.assertEquals(result,null);
    }

    @Test
    public void testDeleteWithExistedID() {
        userServiceImpl.deleteUser();

        Mockito.verify(userPersistence, Mockito.times(1)).delete(user.getId());
    }


    @Test
    public void testFindUserExistedId() {

        User result = userServiceImpl.findUserById(USER_ID);

        Assert.assertEquals(result.getId(), USER_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePasswordIfDifferentUserId() {
        User user = new User();
        user.setId("4");
        userServiceImpl.updatePassword(user);
    }
    @Test
    public void testUpdatePasswordWithExistedUser() {

        Mockito.when(userPersistence.save(user)).thenReturn(user);

        user=userServiceImpl.updatePassword(user);

        Mockito.verify(userPersistence, Mockito.times(1)).save(user);
        Assert.assertEquals(user.getId(), USER_ID);
    }
}