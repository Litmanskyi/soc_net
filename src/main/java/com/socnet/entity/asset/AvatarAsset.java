package com.socnet.entity.asset;

import com.socnet.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter

@Entity
@DiscriminatorValue("AVATAR") //todo +++ in enum
public class AvatarAsset extends ImageAsset {
    public interface AvatarAssetView extends ImageAsset.AssetView {
    }

    @Column(name = "is_current")
    private boolean isCurrent;

    public AvatarAsset() {
    }

    public AvatarAsset(User user, String path, Attached attached) {
        super(user, path, attached);
    }
}
