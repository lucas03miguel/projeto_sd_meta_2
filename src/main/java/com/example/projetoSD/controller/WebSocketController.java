package com.example.projetoSD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

@Controller
public class WebSocketController {
    
    private final SimpMessagingTemplate template;
    
    @Autowired
    public WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }
    
    public void handleSearchMessage(HashMap<String, Integer> topSearches) {
        this.template.convertAndSend("/topic/top-searches", topSearches);
    }
}
