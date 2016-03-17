package com.socnet.persistence.asset;

import com.socnet.entity.asset.AvatarAsset;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AvatarPersistence extends AssetPersistence<AvatarAsset> {
    @Query("SELECT a FROM AvatarAsset AS a WHERE a.user.id = ?1")
    List<AvatarAsset> findAvatarsByUserId(String userId);

    @Query("SELECT a FROM AvatarAsset AS a WHERE a.user.id = ?1 AND a.isCurrent = true")
    AvatarAsset findCurrentAvatarByUserId(String userId);
}
