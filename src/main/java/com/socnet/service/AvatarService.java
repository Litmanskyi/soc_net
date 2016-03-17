package com.socnet.service;

import com.socnet.entity.User;
import com.socnet.entity.asset.AvatarAsset;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AvatarService {
    AvatarAsset setAvatar(MultipartFile multipartFile);

    void deleteAvatar(String id);

    AvatarAsset findAvatarById(String id);

    AvatarAsset findCurrentAvatarByUser(User user);

    List<AvatarAsset> findAvatarsByUser(User user);
}
