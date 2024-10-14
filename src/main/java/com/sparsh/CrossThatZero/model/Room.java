package com.sparsh.CrossThatZero.model;

import com.sparsh.CrossThatZero.dto.WinnerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String crossPlayer;
    private String zeroPlayer;

    private char[] board = {'-', '-', '-', '-', '-', '-', '-', '-', '-'};

    @Enumerated(EnumType.STRING)
    RoomStatus status;
    @Enumerated(EnumType.STRING)
    private WinnerType winnerType;

    public Room(String crossPlayer, String zeroPlayer) {
        this.crossPlayer = crossPlayer;
        this.zeroPlayer = zeroPlayer;
        this.status = RoomStatus.PARTIALLY_COMPLETED;
    }

}



