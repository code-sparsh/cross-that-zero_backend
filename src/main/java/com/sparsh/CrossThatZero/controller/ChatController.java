package com.sparsh.CrossThatZero.controller;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/message")    // /app/message because app is the application prefix
    @SendTo("/chatroom/public")
    private Message receivePublicMessage(@Payload Message message) {

        return message;
    }

}
