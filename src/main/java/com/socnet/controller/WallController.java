package com.socnet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.Post;
import com.socnet.service.WallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/wall")
public class WallController {

    @Autowired
    private WallService wallService;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @JsonView(Post.PostView.class)
    public List<Post> findPosts(@PathVariable("userId") String id) {
        return wallService.getPostsByUserId(id);
    }
}