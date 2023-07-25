package com.sparsh.CrossThatZero.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private UUID id;

    @NotEmpty
    @Size(min = 4)
    private String username;

    @Email(message = "Email is not valid")
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 4, max = 15, message = "Password must be atleast 4 characters and atmost 15 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,15}$")
    private String password;

    /*
    ^           # start-of-string
    (?=.*[0-9])       # a digit must occur at least once
    (?=.*[a-z])       # a lower case letter must occur at least once
    (?=.*[@#$%^&+=])  # a special character must occur at least once
    (?=\S+$)          # no whitespace allowed in the entire string
    $                 # end-of-string
    */

    public UserDto(UUID id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
