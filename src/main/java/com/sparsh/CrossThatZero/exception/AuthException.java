package com.sparsh.CrossThatZero.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthException extends Exception {

    private String errorField;
    private String message;

    public AuthException(String errorField, String message) {
        super(message);
        this.errorField = errorField;
        this.message = message;
    }
}
