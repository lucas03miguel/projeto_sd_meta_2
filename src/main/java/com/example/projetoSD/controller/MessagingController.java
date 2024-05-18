package com.example.projetoSD.controller;

import com.example.projetoSD.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@Controller
public class MessagingController {
    
    private final List<Message> messages = new ArrayList<>();
    private final SimpMessagingTemplate template;
    
    @Autowired
    public MessagingController(SimpMessagingTemplate template) {
        this.template = template;
    }
    
    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public void sendMessage(Message message) {
        messages.add(message);
        this.template.convertAndSend("/topic/messages", message);
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    @GetMapping("/messages")
    public List<Message> getMessagesRest() {
        return getMessages();
    }
}