package com.socnet.persistence.asset;

import com.socnet.entity.asset.Asset;
import com.socnet.entity.enumaration.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

public interface AssetPersistence<T extends Asset> extends JpaRepository<T, String> {

    @Query("select u from #{#entityName} as u where u.attached.id = ?1 and u.assetType = ?2 ")
    T findByAttachedIdAndAssetType(String assetId, AssetType assetType);//todo +++ assetType to enum

    @Query("select u from Asset as u where u.attached.id = ?1")
    List<Asset> findByAttachedId(String assetId);
}
