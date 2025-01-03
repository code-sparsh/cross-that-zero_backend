package com.sparsh.CrossThatZero.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WinnerDto implements RoomMessageDto {

    private String crossPlayer;
    private String zeroPlayer;
    private WinnerType winner;
}
