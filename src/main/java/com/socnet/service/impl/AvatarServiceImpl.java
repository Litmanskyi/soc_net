package com.socnet.service.impl;

import com.socnet.entity.User;
import com.socnet.entity.asset.AvatarAsset;
import com.socnet.persistence.asset.AvatarPersistence;
import com.socnet.service.AvatarService;
import com.socnet.utility.AuthenticatedUtils;
import com.socnet.utility.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;

@Service
public class AvatarServiceImpl implements AvatarService {

    public static final String USER_NOT_FOUND = "User not found!";
    public static final String AVATAR_NOT_FOUND = "Avatar not found!";
    public static final String NOT_YOUR_AVATAR = "It's not your avatar";
    public static final String EMPTY_FILE = "You failed to upload because the file was empty";
    public static final String ONLY_JPG = "Only JPG images are accepted";

    @Autowired
    private AvatarPersistence avatarPersistence;

    @Transactional
    @Override
    public AvatarAsset setAvatar(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_FILE);
        }

        User user = AuthenticatedUtils.getCurrentAuthUser();

        // todo move extra logic in AssetsServices and reuse it on other places in project
        validateImage(file);
        String dir = Paths.get(user.getId(), "avatars/").toString();
        String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String path = FileUtils.uploadFile(dir, fileName, file);
        AvatarAsset avatarAsset = new AvatarAsset();
        avatarAsset.setUser(user);
        avatarAsset.setPath(path);
        avatarAsset.setAttached(user);
        //

        avatarAsset.setCurrent(true);
        AvatarAsset currentAvatarAsset = findCurrentAvatarByUser(user);
        if (currentAvatarAsset != null) {
            currentAvatarAsset.setCurrent(false);
            avatarPersistence.save(currentAvatarAsset);
        }
        return avatarPersistence.save(avatarAsset);
    }

    @Transactional
    @Override
    public void deleteAvatar(String id) {
        User user = AuthenticatedUtils.getCurrentAuthUser();
        AvatarAsset avatar = findAvatarById(id);
        if (avatar == null) {
            throw new EntityNotFoundException(AVATAR_NOT_FOUND);
        }
        if (!avatar.getUser().equals(user)) { // todo create PermissionService.checkPermission(User user, Object bussinesModel, Class clazz){if(clazz == Post.class){}}
            throw new AccessDeniedException(NOT_YOUR_AVATAR);
        }
        FileUtils.deleteFile(avatar.getPath());
        avatarPersistence.delete(id);
    }


    @Override
    public AvatarAsset findAvatarById(String id) {
        return avatarPersistence.findOne(id);
    }


    @Override
    public AvatarAsset findCurrentAvatarByUser(User user) {
        if (user == null) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        return avatarPersistence.findCurrentAvatarByUserId(user.getId());
    }

    @Override
    public List<AvatarAsset> findAvatarsByUser(User user) {
        if (user == null) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        return avatarPersistence.findAvatarsByUserId(user.getId());
    }

    private static void validateImage(MultipartFile image) {
        if (!image.getContentType().equals("image/jpeg")) { // todo move to constants and create method public static boolean isValidFile(File file, Type type -- image, video, ect)
            throw new IllegalArgumentException(ONLY_JPG);
        }
    }
}
