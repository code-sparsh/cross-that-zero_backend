package com.sparsh.CrossThatZero.service;

import org.springframework.stereotype.Service;

@Service
public class GameValidatorService {

    public boolean isGameDone(char[] board) {
        for (int i = 0; i < 9; i++) {
            if (board[i] == '-') {
                return false;
            }
        }
        return true;
    }
}
