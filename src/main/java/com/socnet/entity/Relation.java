package com.socnet.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.enumaration.RelationStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@ToString

@Entity
public class Relation extends BaseEntity {

    public interface RelationView extends User.UserView {
    }

    @JsonView(RelationView.class)
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @JsonView(RelationView.class)
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @JsonView(RelationView.class)
    @Enumerated(EnumType.STRING)
    @Column
    private RelationStatus status;

    public Relation() {
    }

    public Relation(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}
