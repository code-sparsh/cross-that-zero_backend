package com.sparsh.CrossThatZero.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoomDto {

    private String id;
    private String crossPlayer;
    private String zeroPlayer;

    private char[] board;
}
