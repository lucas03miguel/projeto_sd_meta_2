package com.example.projetoSD.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
public class SystemInfoWebSocketHandler extends TextWebSocketHandler {
    
    private final List<WebSocketSession> sessions = new ArrayList<>();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }
    
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle messages from client if needed
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
    
    public void sendSystemInfo(String systemInfo) throws IOException {
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage(systemInfo));
        }
    }
}
