package com.sparsh.CrossThatZero.dto;

import com.sparsh.CrossThatZero.model.Room;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoomDto {

    private String crossPlayer;
    private String zeroPlayer;

    private char[] board;
}
