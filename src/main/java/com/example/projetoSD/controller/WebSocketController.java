package com.example.projetoSD.controller;

import com.example.projetoSD.service.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.List;

@Controller
public class WebSocketController {
    
    @Autowired
    public WebSocketController() {
    }
    
    @MessageMapping("/top10searches")
    @SendTo("/topic/top10searches")
    public HashMap<String, Integer> sendTop10Searches(HashMap<String, Integer> topSearches) {
        System.out.println("Sending top 10 searches");
        System.out.println(topSearches);
        System.out.println(new Message(topSearches).content());
        return new Message(topSearches).content();
        //return topSearches;
    }
}
