package com.sparsh.CrossThatZero.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class QueuePlayer implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID sessionID;
    private String username;
    private Room room;

    public QueuePlayer(UUID sessionID, String username, Room room) {
        this.sessionID = sessionID;
        this.username = username;
        this.room = room;
    }

    public QueuePlayer() {
    }

}
