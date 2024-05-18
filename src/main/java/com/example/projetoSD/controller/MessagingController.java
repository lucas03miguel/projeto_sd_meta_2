package com.example.projetoSD.controller;

import com.example.projetoSD.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessagingController {
    // TODO: implement the onMessage method. Don't forget the annotations.
    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message onMessage(Message message) throws InterruptedException {
        System.out.println("Message received " + message);
        Thread.sleep(1000); // simulated delay
        return new Message(HtmlUtils.htmlEscape(message.content()));
    }
}
