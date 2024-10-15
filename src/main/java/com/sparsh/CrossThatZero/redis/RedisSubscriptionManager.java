package com.sparsh.CrossThatZero.redis;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisSubscriptionManager {

    private final RedisMessageListenerContainer redisContainer;
    private final MessageListenerAdapter listenerAdapter;
    private final Map<String, ChannelTopic> activeRooms = new HashMap<>();

    public RedisSubscriptionManager(RedisMessageListenerContainer redisContainer, MessageListenerAdapter listenerAdapter) {
        this.redisContainer = redisContainer;
        this.listenerAdapter = listenerAdapter;
    }

    public void subscribeToRoom(String roomID) {

        String channelName = "room:" + roomID;
        subscribeToChannel(channelName);
    }

    public void unsubscribeFromRoom(String roomID) {
        String channelName = "room:" + roomID;
        unsubscribeFromChannel(channelName);
    }

    private void subscribeToChannel(String channelName) {
        ChannelTopic topic = new ChannelTopic(channelName);
        if (!activeRooms.containsKey(channelName)) {
            redisContainer.addMessageListener(listenerAdapter, topic);
            activeRooms.put(channelName, topic);
            System.out.println("Subscribing to channel " + channelName);
        }
    }

    private void unsubscribeFromChannel(String channelName) {
        ChannelTopic topic = activeRooms.get(channelName);
        if (topic != null) {
            redisContainer.removeMessageListener(listenerAdapter, topic);
            activeRooms.remove(channelName);
            System.out.println("Unsubscribing from channel " + channelName);
        }
    }


}
