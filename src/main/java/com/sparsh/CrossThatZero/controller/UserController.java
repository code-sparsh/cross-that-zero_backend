package com.sparsh.CrossThatZero.controller;

import com.sparsh.CrossThatZero.dto.UserDto;
import com.sparsh.CrossThatZero.exception.AuthException;
import com.sparsh.CrossThatZero.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody UserDto userDto) throws AuthException {
        UserDto userDto1 = userService.createUser(userDto);
        return new ResponseEntity<UserDto>(userDto1, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<UserDto> login(@Valid @RequestBody UserDto userDto) throws AuthException {
        UserDto userDto1 = userService.login(userDto);
        return new ResponseEntity<UserDto>(userDto1, HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "yes?";
    }
}
