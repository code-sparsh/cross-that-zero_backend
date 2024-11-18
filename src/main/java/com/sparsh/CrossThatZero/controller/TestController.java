package com.sparsh.CrossThatZero.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public ResponseEntity<?> helloWorld() {
        return ResponseEntity.status(200).body("Hello World");
    }


}
