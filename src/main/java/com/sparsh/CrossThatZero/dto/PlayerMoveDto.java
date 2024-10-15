package com.sparsh.CrossThatZero.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayerMoveDto {

    private String move;
    private PlayerType playerType;

    public PlayerMoveDto() {
    }
}
