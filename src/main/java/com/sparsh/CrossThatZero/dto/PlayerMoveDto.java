package com.sparsh.CrossThatZero.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayerMoveDto {

    private String move;

    public PlayerMoveDto() {
    }
}
