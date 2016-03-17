package com.socnet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.Comment;
import com.socnet.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment/")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @JsonView(Comment.CommentView.class)
    @RequestMapping(value = "{postId}", method = RequestMethod.POST)
    public Comment addCommentToPost(@PathVariable("postId") String postId, @RequestBody Comment comment) {
        return commentService.addCommentToPost(comment, postId);
    }

    @JsonView(Comment.CommentView.class)
    @RequestMapping(value = "{commentId}", method = RequestMethod.DELETE)
    public void deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId);
    }
}
