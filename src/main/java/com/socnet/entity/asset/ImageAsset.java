package com.socnet.entity.asset;

import com.socnet.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter

@Entity
@DiscriminatorValue("IMAGE")
public class ImageAsset extends Asset {
    public interface ImageAssetView extends AssetView {
    }

    public ImageAsset() {
    }

    public ImageAsset(User user, String path, Attached attached) {
        super(user, path, attached);
    }
}
