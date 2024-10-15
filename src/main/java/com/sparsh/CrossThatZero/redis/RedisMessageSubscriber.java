package com.sparsh.CrossThatZero.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparsh.CrossThatZero.repository.RoomRepository;
import com.sparsh.CrossThatZero.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    @Lazy
    private SocketService socketService;

    public void onMessage(String message, String channel) throws JsonProcessingException {
        System.out.println(message + " from " + channel);
        String roomID = channel.substring(5);

        socketService.broadcastRoomMessage(message);
    }
}
