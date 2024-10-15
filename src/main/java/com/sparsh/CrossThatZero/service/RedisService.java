package com.sparsh.CrossThatZero.service;

import com.sparsh.CrossThatZero.model.QueuePlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final String QUEUE_KEY = "player-queue";

    @Autowired
    private RedisTemplate redisTemplate;

    public void addToQueue(QueuePlayer value) {
        redisTemplate.opsForList().rightPush(QUEUE_KEY, value);
    }

    public QueuePlayer removeFromQueue() {
        return (QueuePlayer) redisTemplate.opsForList().leftPop(QUEUE_KEY);
    }

    public void put(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }


}
