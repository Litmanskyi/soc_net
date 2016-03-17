package com.socnet.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.asset.Asset;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter

@Entity
public class Comment extends AbstractContent {

    public interface CommentView extends AbstractContent.AbstractContentView {
    }

    @JsonView(CommentView.class)
    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Transient
    @JsonView(CommentView.class)
    private List<Asset> assets;
}
