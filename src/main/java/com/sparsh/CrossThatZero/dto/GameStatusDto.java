package com.sparsh.CrossThatZero.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameStatusDto implements RoomMessageDto {

    private GameStatusType status;
    private String crossPlayer;
    private String zeroPlayer;
}
