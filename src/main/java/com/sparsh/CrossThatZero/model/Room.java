package com.sparsh.CrossThatZero.model;

import com.sparsh.CrossThatZero.dto.WinnerType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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
        this.id = UUID.randomUUID();
    }

}



