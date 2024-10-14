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

    public boolean isCrossWinner(char[] boardArray) {
        char[][] board = new char[3][3];

        for (int i = 0; i < 3; i++) {
            System.arraycopy(boardArray, i * 3, board[i], 0, 3);
        }
        return checkRows(board, 'X') || checkColumns(board, 'X') || checkDiagonals(board, 'X');
    }

    public boolean isZeroWinner(char[] boardArray) {
        char[][] board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(boardArray, i * 3, board[i], 0, 3);
        }
        return checkRows(board, '0') || checkColumns(board, '0') || checkDiagonals(board, '0');
    }


    public boolean checkRows(char[][] board, char type) {

        for (int i = 0; i < 3; i++) {
            boolean check = true;
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != type) {
                    check = false;
                    break;
                }
            }

            if (check)
                return true;
        }
        return false;
    }

    public boolean checkColumns(char[][] board, char type) {
        for (int i = 0; i < 3; i++) {
            boolean check = true;
            for (int j = 0; j < 3; j++) {
                if (board[j][i] != type) {
                    check = false;
                    break;
                }
            }
            if (check)
                return true;
        }
        return false;
    }

    public boolean checkDiagonals(char[][] board, char type) {

        if (board[0][0] == type && board[1][1] == type && board[2][2] == type)
            return true;

        else return board[0][2] == type && board[1][1] == type && board[2][0] == type;
    }
}


