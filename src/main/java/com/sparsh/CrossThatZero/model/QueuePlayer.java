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

    public QueuePlayer(UUID sessionID, String username) {
        this.sessionID = sessionID;
        this.username = username;
    }

    public QueuePlayer() {
    }

}
