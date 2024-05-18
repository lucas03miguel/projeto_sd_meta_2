package com.example.projetoSD.service;


import com.example.projetoSD.handler.SystemInfoWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SystemInfoService {
    private final SystemInfoWebSocketHandler webSocketHandler;
    
    @Autowired
    public SystemInfoService(SystemInfoWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }
    
    @Scheduled(fixedRate = 5000)
    public void updateSystemInfo() {
        // Aqui você coleta as informações do sistema, por exemplo:
        String systemInfo = getSystemInfo();
        try {
            webSocketHandler.sendSystemInfo(systemInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String getSystemInfo() {
        // Simular a obtenção de informações do sistema
        return "{\"searches\":[\"query1\", \"query2\"], \"activeBarrels\":5, \"responseTimes\":{\"barrel1\":0.5, \"barrel2\":0.6}}";
    }
}
