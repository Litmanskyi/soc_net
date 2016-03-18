package com.socnet.service.permission;

import com.socnet.entity.*;
import com.socnet.entity.asset.Asset;

public interface PermissionService {

    boolean checkPostPermission(User user, Post post);

    boolean checkAssetPermission(User user, Asset asset);

    boolean checkCommentPermission(User user, Comment comment);

    boolean checkRoomPermission(User user, Room room);
}
