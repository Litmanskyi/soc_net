package com.socnet.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.asset.Asset;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.util.List;

@Getter
@Setter

@Entity
public class Message extends AbstractContent  {

    public interface MessageView extends AbstractContentView {
    }

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Transient
    @JsonView(MessageView.class)
    private List<Asset> assets;


    public Message() {
    }

    public Message(User user, Room room, String message) {
        this.setCreator(user);
        this.setMessage(message);
        this.room = room;
    }

}
