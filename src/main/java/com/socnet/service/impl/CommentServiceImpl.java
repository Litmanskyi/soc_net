package com.socnet.service.impl;

import com.socnet.entity.Comment;
import com.socnet.entity.Post;
import com.socnet.entity.User;
import com.socnet.persistence.CommentPersistence;
import com.socnet.service.CommentService;
import com.socnet.service.PostService;
import com.socnet.service.permission.PermissionService;
import com.socnet.utility.AuthenticatedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class CommentServiceImpl implements CommentService {

    public static final String POST_NOT_FOUND = "Post not found!";
    public static final String COMMENT_NOT_FOUND = "Comment not found!";
    public static final String YOU_DON_T_HAVE_ENOUGH_RIGHTS = "You don't have enough rights!";

    @Autowired
    private CommentPersistence commentPersistence;

    @Autowired
    private PostService postService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public Comment addCommentToPost(Comment comment, String postId) {
        User currentUser = AuthenticatedUtils.getCurrentAuthUser();

        Post post = postService.findPost(postId);
        if (post == null) {
            throw new EntityNotFoundException(POST_NOT_FOUND);
        }

        post.getComments().add(comment);
        comment.setPost(post);
        comment.setCreator(currentUser);
        return commentPersistence.save(comment);
    }

    @Override
    public void deleteComment(String commentId) {
        Comment comment = findComment(commentId);

        if (comment == null) {
            throw new EntityNotFoundException(COMMENT_NOT_FOUND);
        }

        User currentUser = AuthenticatedUtils.getCurrentAuthUser();
        //todo +++ permission
        if(permissionService.checkCommentPermission(currentUser, comment)){
            throw new AccessDeniedException(YOU_DON_T_HAVE_ENOUGH_RIGHTS);
        }

        commentPersistence.delete(comment);
    }

    @Override
    public Comment findComment(String commentId) {
        return commentPersistence.findOne(commentId);
    }
}
