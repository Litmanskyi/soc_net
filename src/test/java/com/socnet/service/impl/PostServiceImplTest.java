package com.socnet.service.impl;

import com.socnet.entity.Post;
import com.socnet.entity.User;
import com.socnet.entity.Wall;
import com.socnet.persistence.PostPersistence;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthenticatedUtils.class)
public class PostServiceImplTest {
    public static final String NOT_CORRECT_USER_ID = "5";
    private final String POST_ID = "1";
    private final String USER_ID = "3";
    private final String WALL_ID = "12";
    private final String POST_MESSAGE = "message";
    private static User user;
    private static Post post;
    private static Wall wall;

    @Mock
    private PostPersistence postPersistence;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostServiceImpl postService;


    @Before
    public void setUp() {
        user = new User();
        user.setId(USER_ID);
        post = new Post();
        post.setId(POST_ID);
        wall = new Wall();
        wall.setId(WALL_ID);

        PowerMockito.mockStatic(AuthenticatedUtils.class);
        BDDMockito.given(AuthenticatedUtils.getCurrentAuthUser()).willReturn(user);

    }

    @Test
    public void testFindPostWithExistedId() {
        Mockito.when(postPersistence.findOne(POST_ID)).thenReturn(post);
        post = postService.findPost(POST_ID);

        Mockito.verify(postPersistence, Mockito.times(1)).findOne(POST_ID);
        Assert.assertEquals(post.getId(), POST_ID);
    }

    @Test
    public void testAddPostToWallWithExistedUser() {
        wall.getPosts().add(post);
        wall.setUser(user);
        user.setWall(wall);

        Mockito.when(userService.findUserById(USER_ID)).thenReturn(user);
        Mockito.when(postPersistence.save(post)).thenReturn(post);

        post = postService.addPostToUserWall(USER_ID, post);

        Mockito.verify(userService, Mockito.times(1)).findUserById(USER_ID);
        Mockito.verify(postPersistence, Mockito.times(1)).save(post);

        Assert.assertEquals(post.getId(), POST_ID);
        Assert.assertEquals(post.getWall().getId(), WALL_ID);
        Assert.assertEquals(post.getWall().getUser().getId(), USER_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeletePostWithoutRequiredPermission() {
        User user = new User();
        user.setId(NOT_CORRECT_USER_ID);
        post.setCreator(user);
        wall.setUser(user);
        post.setWall(wall);
        Mockito.when(postPersistence.findOne(POST_ID)).thenReturn(post);
        postService.deletePost(POST_ID);
    }

    @Test
    public void testDeletePostWithCorrectUserCreator() {
        post.setCreator(user);

        Mockito.when(postPersistence.findOne(POST_ID)).thenReturn(post);
        postService.deletePost(POST_ID);

        Mockito.verify(postPersistence, Mockito.times(1)).findOne(POST_ID);
        Mockito.verify(postPersistence, Mockito.times(1)).delete(POST_ID);
    }

    @Test
    public void testDeletePostWithCorrectUserWall() {
        User userPost = new User();
        userPost.setId(NOT_CORRECT_USER_ID);
        post.setCreator(userPost);
        post.setWall(wall);
        wall.setUser(user);

        Mockito.when(postPersistence.findOne(POST_ID)).thenReturn(post);
        postService.deletePost(POST_ID);

        Mockito.verify(postPersistence, Mockito.times(1)).findOne(POST_ID);
        Mockito.verify(postPersistence, Mockito.times(1)).delete(POST_ID);
    }

    @Test
    public void testEditPostWithCorrectUserCreator() {
        post.setMessage(POST_MESSAGE);
        post.setCreator(user);

        Mockito.when(postPersistence.save(post)).thenReturn(post);
        Mockito.when(postPersistence.findOne(POST_ID)).thenReturn(post);
        post = postService.editPost(post);

        Mockito.verify(postPersistence, Mockito.times(1)).findOne(POST_ID);
        Mockito.verify(postPersistence, Mockito.times(1)).save(post);
        Assert.assertEquals(post.getMessage(), POST_MESSAGE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditPostWithIncorrectUserCreator() {
        User userPost = new User();
        userPost.setId(NOT_CORRECT_USER_ID);
        post.setCreator(userPost);

        Mockito.when(postPersistence.findOne(POST_ID)).thenReturn(post);

        postService.editPost(post);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePostWithoutUserCreator() {
        post.setWall(wall);

        postService.createPost(post);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePostWithoutWall() {
        Post post = new Post();
        post.setId(POST_ID);
        User user = new User();
        user.setId(USER_ID);
        post.setCreator(user);

        postService.createPost(post);
    }
}