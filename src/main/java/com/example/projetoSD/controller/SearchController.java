package com.example.projetoSD.controller;

import com.example.projetoSD.interfaces.RMIServerInterface;
import com.example.projetoSD.model.IndexedUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class SearchController {
    
    private final RMIServerInterface sv;
    private final SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    public SearchController(RMIServerInterface rmisv, SimpMessagingTemplate messagingTemplate) {
        this.sv = rmisv;
        this.messagingTemplate = messagingTemplate;
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
    
    @GetMapping("/search-url")
    @ResponseBody
    public ResponseEntity<List<String>> searchUrl(@RequestParam String url) {
        try {
            List<String> urls = sv.obterLigacoes(url);
            return ResponseEntity.ok(urls);
        } catch (RemoteException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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
            messagingTemplate.convertAndSend("/topic/update-top-searches", query);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @GetMapping("/top-searches")
    public String topSearches(Model model) throws RemoteException {
        model.addAttribute("topSearches", sv.obterTopSearches());
        return "top-searches";
    }
    
    private void updateTopSearches() {
        messagingTemplate.convertAndSend("/topic/update-top-searches", "update");
    }
    
    @GetMapping("/top-searches-list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTopSearches() {
        try {
            HashMap<String, Integer> results = sv.obterTopSearches();
            Map<String, Object> response = new HashMap<>();
            response.put("results", results);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @GetMapping("/barrels")
    public String barrelsPage(Model model) throws RemoteException {
        model.addAttribute("barrels", sv.obterListaBarrels());
        return "barrels";
    }
    
    @GetMapping("/barrels-list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> barrels(Model model) {
        try {
            List<String> barrels = sv.obterListaBarrels();
            Map<String, Object> response = new HashMap<>();
            response.put("results", barrels);
            model.addAttribute("barrels", barrels);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
