package com.socnet.service;

import com.socnet.entity.asset.Asset;

import java.util.List;

public interface AssetService {
    List<Asset> findAssetsByAttachedId(String attachedId);
}
