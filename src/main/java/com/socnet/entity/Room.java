package com.socnet.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter

@Entity
public class Room extends BaseEntity {

    public interface RoomView extends User.UserView {
    }

    public interface RoomMessageView extends AbstractContent.AbstractContentView, RoomView {
    }

    @JsonView(RoomView.class)
    @Column(length = 200)
    @Size(max = 200)
    private String title;

    @JsonView(RoomMessageView.class)
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @JsonView(RoomView.class)
    @ManyToMany
    @JoinTable(name = "user_room",
            joinColumns = @JoinColumn(name = "room_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id"))
    private Set<User> users;

    @JsonView(RoomView.class)
    @ManyToOne
    @JoinColumn(nullable = false, name = "admin_id")
    private User admin;

    @JsonView(RoomView.class)
    @Column
    private boolean isDialog;

    public Room() {
        users = new HashSet<>();
        messages = new ArrayList<>();
    }
}
