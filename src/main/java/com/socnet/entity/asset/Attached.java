package com.socnet.entity.asset;

import java.util.List;

public interface Attached {
    void setAssets(List<Asset> assets);

    List<Asset> getAssets();

    String getId();
}
