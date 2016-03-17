package com.socnet.service;

import com.socnet.entity.Post;
import com.socnet.entity.Wall;

import java.util.List;

public interface WallService {
    Wall createWall(Wall wall);

    Wall getWallByUserId(String id);

    List<Post> getPostsByUserId(String userId);
}
