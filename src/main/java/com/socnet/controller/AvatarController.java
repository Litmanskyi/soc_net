package com.socnet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.asset.AvatarAsset;
import com.socnet.service.AvatarService;
import com.socnet.utility.AuthenticatedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/avatar/")
public class AvatarController {

    @Autowired
    private AvatarService avatarService;

    @RequestMapping(method = RequestMethod.POST)
    @JsonView(AvatarAsset.AvatarAssetView.class)
    public AvatarAsset avatarUpload(@RequestParam("file") MultipartFile file) {
        return avatarService.setAvatar(file);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public void deleteAvatar(@PathVariable("id") String id) {
        avatarService.deleteAvatar(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    @JsonView(AvatarAsset.AvatarAssetView.class)
    public AvatarAsset findCurrentAvatar() {
        return avatarService.findCurrentAvatarByUser(AuthenticatedUtils.getCurrentAuthUser());
    }
}
