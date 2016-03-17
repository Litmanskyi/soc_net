package com.socnet.entity.asset;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.MetaValue;

import javax.persistence.*;

@Getter
@Setter

@Entity
@Table
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 8)
public class Asset extends BaseEntity {
    public interface AssetView extends BaseView {
    }

    @Any(
            metaColumn = @Column(name = "asset_type", length = 3),
            fetch = FetchType.LAZY
    )
    @AnyMetaDef(
            idType = "string", metaType = "string",
            metaValues = {
                    @MetaValue(targetEntity = Post.class, value = "PST"), // todo create ENUM for assets type
                    @MetaValue(targetEntity = Message.class, value = "MSG"),
                    @MetaValue(targetEntity = Comment.class, value = "CMT"),
                    @MetaValue(targetEntity = User.class, value = "USR")
            }
    )
    @JoinColumn(name = "asset_id", referencedColumnName = "id")
    private Attached attached;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonView(AssetView.class)
    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    @JsonView(AssetView.class)
    @Column(name = "asset_type", insertable = false, updatable = false)
    private String assetType;

    @JsonView(AssetView.class)
    @Column(name = "path", nullable = false, length = 255)
    private String path;
}
