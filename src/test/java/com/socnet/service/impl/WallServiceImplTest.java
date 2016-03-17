package com.socnet.service.impl;

import com.socnet.entity.Post;
import com.socnet.entity.User;
import com.socnet.entity.Wall;
import com.socnet.persistence.UserPersistence;
import com.socnet.persistence.WallPersistence;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WallServiceImplTest {
    private final String USER_ID="1";
    private final String WALL_ID = "1";
    private final String POST_ID = "1";
    private static User user;
    private static Wall wall;
    private static Post post;
    @Mock
    private UserPersistence userPersistence;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private WallPersistence wallPersistence;

    @InjectMocks
    private WallServiceImpl wallService;

    @Before
    public void setUp() {
        user = new User();
        user.setId(USER_ID);
        post = new Post();
        post.setId(POST_ID);
        wall = new Wall();
        wall.setId(WALL_ID);

        Mockito.when(userPersistence.findOne(USER_ID)).thenReturn(user);
    }

    @Test
    public void testFindWallWithExistedUserId() {
        user.setWall(wall);

        Mockito.when(userService.findUserById(USER_ID)).thenReturn(user);

        wall = wallService.getWallByUserId(USER_ID);

        Mockito.verify(userService, Mockito.times(1)).findUserById(USER_ID);
        Assert.assertEquals(wall.getId(), WALL_ID);
    }

    @Test
    public void testFindPostsWithExistedWall() {
        user.setWall(wall);
        wall.getPosts().add(post);

        Mockito.when(userService.findUserById(USER_ID)).thenReturn(user);
        List<Post> postList = wallService.getPostsByUserId(USER_ID);
        Mockito.verify(userService, Mockito.times(1)).findUserById(USER_ID);
        Assert.assertEquals(postList.get(0).getId(),POST_ID);

    }
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWallWithoutUser() {
        wallService.createWall(wall);
    }

}