package com.socnet.service.impl;


import com.socnet.entity.Post;
import com.socnet.entity.User;
import com.socnet.entity.Wall;
import com.socnet.persistence.WallPersistence;
import com.socnet.service.PostService;
import com.socnet.service.UserService;
import com.socnet.service.WallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class WallServiceImpl implements WallService {

    public static final String USER_NOT_FOUND = "User not found!";

    @Autowired
    WallPersistence wallPersistence;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    //todo delete
    @Override
    @Transactional
    public Wall createWall(Wall wall) {
        if (wall.getUser() == null) {
            throw new IllegalArgumentException("Wall without user!");
        }
        return wallPersistence.save(wall);
    }

    @Override
    public Wall getWallByUserId(String id) {
        User user = userService.findUserById(id);
        if (user == null) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        return user.getWall();
    }

    @Override
    public List<Post> getPostsByUserId(String userId) {
        Wall wall = getWallByUserId(userId);
        return wall.getPosts();
    }

}
