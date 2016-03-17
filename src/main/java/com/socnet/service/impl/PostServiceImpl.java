package com.socnet.service.impl;


import com.socnet.entity.Post;
import com.socnet.entity.User;
import com.socnet.persistence.PostPersistence;
import com.socnet.service.PostService;
import com.socnet.service.UserService;
import com.socnet.service.WallService;
import com.socnet.utility.AuthenticatedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class PostServiceImpl implements PostService {

    public static final String USER_NOT_FOUND = "User not found!";
    public static final String POST_NOT_FOUND = "The post doesn't exist!!";

    @Autowired
    private PostPersistence postPersistence;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public Post editPost(Post p) {

        Post post = findPost(p.getId());

        User currentAuthUser = AuthenticatedUtils.getCurrentAuthUser();
        if (post == null) {
            throw new EntityNotFoundException(POST_NOT_FOUND);
        }
        if (!post.getCreator().getId().equals(currentAuthUser.getId())) {
            throw new IllegalArgumentException("Insufficient rights for editing!");
        }

        post.setMessage(p.getMessage());
        return postPersistence.save(post);
    }


    @Transactional
    @Override
    public void deletePost(String postId) {
        Post post = findPost(postId);
        User user = AuthenticatedUtils.getCurrentAuthUser();
        if (post == null) {
            throw new EntityNotFoundException(POST_NOT_FOUND);
        }
        if (!(post.getCreator().getId().equals(user.getId())
                || post.getWall().getUser().getId().equals(user.getId()))) {
            throw new IllegalArgumentException("Insufficient rights for editing!");
        }
        postPersistence.delete(postId);
    }

    @Override
    public Post findPost(String id) {
        return postPersistence.findOne(id);
    }

    /**
     * @param userId user on whose wall the post will be attached
     * @param post      post which will be attached
     * @return Object Post with postId, wallId
     */
    @Override
    public Post addPostToUserWall(String userId, Post post) {
        User user = userService.findUserById(userId);
        User creatorPost = AuthenticatedUtils.getCurrentAuthUser();
        //todo check permission on relation
        if (user == null) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        post.setCreator(creatorPost);
        post.setWall(user.getWall());
        return postPersistence.save(post);
    }
}
