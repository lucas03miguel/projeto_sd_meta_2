package com.example.projetoSD.controller;

import com.example.projetoSD.interfaces.RMIServerInterface;
import com.example.projetoSD.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Controller
public class MessagingController {
    
    private HashMap<String, Integer> messages;
    private final SimpMessagingTemplate template;
    private final RMIServerInterface sv;
    
    @Autowired
    public MessagingController(SimpMessagingTemplate template, RMIServerInterface sv) {
        this.template = template;
        this.sv = sv;
    }
    
    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public void sendMessage(Message message) {
        try {
            messages = this.sv.obterTopSearches();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Message received " + message);
        this.template.convertAndSend("/topic/messages", message);
    }
    
    public HashMap<String, Integer> getMessages() {
        return messages;
    }
    
    @GetMapping("/messages")
    public HashMap<String, Integer> getMessagesRest() {
        return getMessages();
    }
}
