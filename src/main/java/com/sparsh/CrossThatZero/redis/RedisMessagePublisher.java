package com.sparsh.CrossThatZero.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher {

    private final RedisTemplate redisTemplate;

    @Autowired
    public RedisMessagePublisher(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publish(String roomID, Object message) {
        String channel = "room:" + roomID;
        ChannelTopic topic = new ChannelTopic(channel);
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }

}
