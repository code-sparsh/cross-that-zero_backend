package com.sparsh.CrossThatZero.controller;

import com.sparsh.CrossThatZero.redis.RedisMessagePublisher;
import com.sparsh.CrossThatZero.redis.RedisMessageSubscriber;
import com.sparsh.CrossThatZero.redis.RedisSubscriptionManager;
import com.sparsh.CrossThatZero.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisMessagePublisher redisMessagePublisher;

    @Autowired
    private RedisMessageSubscriber redisMessageSubscriber;

    @Autowired
    private RedisSubscriptionManager redisSubscriptionManager;

//    @GetMapping
//    public ResponseEntity<QueuePlayer> test() {
//        redisService.addToQueue(new QueuePlayer(UUID.randomUUID(), "sparsh"));
//        return ResponseEntity.status(200).body(redisService.removeFromQueue());
//    }

    @PostMapping
    public ResponseEntity<?> test(@RequestBody String roomID) {

        System.out.println(roomID);

        roomID = roomID.trim();

        redisSubscriptionManager.subscribeToRoom(roomID);
        redisMessagePublisher.publish(roomID, "hi there");

        return ResponseEntity.status(200).body("Hello World");
    }


}
