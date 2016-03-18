package com.socnet.service.permission.impl;

import com.socnet.entity.*;
import com.socnet.entity.asset.Asset;
import com.socnet.service.permission.PermissionService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Override
    public boolean checkPostPermission(User user, Post post) {
        return true;
    }

    @Override
    public boolean checkAssetPermission(User user, Asset asset) {
        if (asset.getUser().equals(user)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkCommentPermission(User user, Comment comment) {
        if (comment.getCreator().equals(user) || comment.getPost().getWall().getUser().equals(user)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkRoomPermission(User user, Room room) {
        if (room.getUsers().contains(user)) {
            return true;
        }
        return false;
    }
}
