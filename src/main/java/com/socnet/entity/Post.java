package com.socnet.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.asset.Asset;
import com.socnet.entity.asset.Attached;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Setter
@Getter
@lombok.ToString(exclude = {"wall"})

@Entity
public class Post extends AbstractContent implements Attached {

    public interface PostView extends Wall.WallView, AbstractContent.AbstractContentView{
    }

    @JsonView(PostView.class)
    @ManyToOne
    @JoinColumn(name = "wall_id", nullable = false)
    private Wall wall;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @Transient
    @JsonView(PostView.class)
    private List<Asset> assets;
}
