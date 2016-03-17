package com.socnet.entity.asset;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("VIDEO")
public class VideoAsset extends Asset {
}
