package com.example.websocketdemo.controller;

import com.example.websocketdemo.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/sendMessage") // Clients send messages here
    @SendTo("/topic/messages") // Messages will be broadcasted here
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }
}
