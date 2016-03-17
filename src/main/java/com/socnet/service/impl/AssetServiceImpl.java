package com.socnet.service.impl;

import com.socnet.entity.asset.Asset;
import com.socnet.persistence.asset.AssetPersistence;
import com.socnet.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService{
    @Autowired
    private AssetPersistence assetPersistence;

    public List<Asset> findAssetsByAttachedId(String attachedId){
        return assetPersistence.findByAttachedId(attachedId);
    }
}
