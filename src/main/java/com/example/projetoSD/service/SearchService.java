package com.example.projetoSD.service;


import com.example.projetoSD.model.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SearchService {
    
    public List<Message> search(HashMap<String, Integer> query) {
        // Lógica de busca
        List<Message> results = new ArrayList<>();
        for (String key : query.keySet()) {
            results.add(new Message(key));
        }
        
        return results;
    }
}
