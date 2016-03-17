package com.socnet.service;

import com.socnet.entity.Comment;

public interface CommentService {
    Comment addCommentToPost(Comment comment,String postId);

    void deleteComment(String commentId);

    Comment findComment(String commentId);
}
