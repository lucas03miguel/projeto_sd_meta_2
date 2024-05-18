package com.example.projetoSD.controller;

import com.example.projetoSD.model.Message;
import com.example.projetoSD.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;

@Controller
public class WebSocketController {
    
    @Autowired
    private SearchService searchService;
    
    @MessageMapping("/search")
    @SendTo("/topic/searchResults")
    public List<Message> handleSearchMessage(HashMap<String, Integer> message) {
        return searchService.search(message);
    }
}

