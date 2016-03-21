package com.socnet.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.asset.Attached;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter

@MappedSuperclass
public abstract class AbstractContent extends BaseEntity implements Attached{

    public interface AbstractContentView extends User.UserView {}

    @JsonView(AbstractContentView.class)
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 1000)
    @Column(length = 1000, nullable = false)
    private String message;

    @JsonView(AbstractContentView.class)
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
}
