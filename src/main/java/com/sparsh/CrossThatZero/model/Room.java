package com.sparsh.CrossThatZero.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int playerCount;

    private String crossPlayer;
    private String zeroPlayer;

    private char[] board = {'-', '-', '-', '-', '-', '-', '-', '-', '-'};

}
