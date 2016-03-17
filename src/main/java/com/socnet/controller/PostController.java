package com.socnet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.Post;
import com.socnet.service.PostService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/post/")
public class PostController {

    private Logger logger = Logger.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    /**
     * @param id   userId on whose wall is created post
     * @param post valid post
     */
    @RequestMapping(value = "{userId}", method = RequestMethod.POST)
    @JsonView(Post.PostView.class)
    public Post addPost(@PathVariable("userId") String id, @RequestBody @Valid Post post) {
        return postService.addPostToUserWall(id, post);
    }

    @RequestMapping(value = "{postId}", method = RequestMethod.DELETE)
    public void deletePost(@PathVariable("postId") String postId) {
        postService.deletePost(postId);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @JsonView(Post.PostView.class)
    public Post editPost(@RequestBody(required = true) Post post) {
        return postService.editPost(post);
    }
}
