package com.sparsh.CrossThatZero.controller;

import com.sparsh.CrossThatZero.model.QueuePlayer;
import com.sparsh.CrossThatZero.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private RedisService redisService;

    @GetMapping
    public ResponseEntity<QueuePlayer> test() {
        redisService.addToQueue(new QueuePlayer(UUID.randomUUID(), "sparsh"));
        return ResponseEntity.status(200).body(redisService.removeFromQueue());
    }
}
