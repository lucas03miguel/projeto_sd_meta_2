package com.example.projetoSD.controller;

import com.example.projetoSD.interfaces.RMIServerInterface;
import com.example.projetoSD.model.IndexedUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;
import java.util.*;

@Controller
@RequestMapping("/")
public class SearchController {
    
    private final RMIServerInterface sv;
    private final WebSocketController webSocketController;
    
    @Autowired
    public SearchController(RMIServerInterface rmisv, WebSocketController webSocketController) {
        this.sv = rmisv;
        this.webSocketController = webSocketController;
    }
    
    @PostMapping("/index-url")
    @ResponseBody
    public ResponseEntity<String> indexUrl(@RequestBody IndexedUrl indexedUrl) {
        try {
            String res = sv.indexar(indexedUrl.getUrl());
            if (res.equals("URL valido")) {
                return ResponseEntity.ok("URL adicionado com sucesso!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to index URL.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to index URL.");
        }
    }
    
    private void updateTopSearches() {
        System.out.println("Updating top searches");
        // Suponha que você tenha uma maneira de obter as top searches
        HashMap<String, Integer> topSearches; // Método fictício, adapte conforme necessário
        try {
            topSearches = sv.obterTopSearches();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        webSocketController.handleSearchMessage(topSearches);
    }
    
    @GetMapping("/search")
    public String searchPage(@RequestParam String query, @RequestParam int page, Model model) {
        model.addAttribute("query", query);
        model.addAttribute("page", page);
        updateTopSearches();
        return "search";
    }
    
    @GetMapping("/search-results")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchResults(@RequestParam String query, @RequestParam int page) {
        try {
            HashMap<String, ArrayList<String>> results = sv.pesquisar(query);
            
            int pageSize = 10;
            List<Map.Entry<String, ArrayList<String>>> resultList = new ArrayList<>(results.entrySet());
            int fromIndex = page * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, resultList.size());
            
            HashMap<String, ArrayList<String>> paginatedResults = new HashMap<>();
            for (int i = fromIndex; i < toIndex; i++) {
                Map.Entry<String, ArrayList<String>> entry = resultList.get(i);
                paginatedResults.put(entry.getKey(), entry.getValue());
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("results", paginatedResults);
            response.put("isLastPage", toIndex == resultList.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @GetMapping("/top-searches")
    public String topSearches(Model model) {
        return "top-searches";
    }
    
    
}
