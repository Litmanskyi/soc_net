package com.socnet.service;

import com.socnet.entity.Post;

public interface PostService {
    Post createPost(Post post);

    Post editPost(Post post);

    void deletePost(String postId);

    Post findPost(String id);

    Post addPostToUserWall(String userId, Post p);
}
