package com.example.projetoSD.controller;

import com.example.projetoSD.interfaces.RMIServerInterface;
import com.example.projetoSD.model.IndexedUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class SearchController {
    private final RMIServerInterface sv;

    @Autowired
    SearchController(RMIServerInterface rmiServerInterface) {
        this.sv = rmiServerInterface;
    }

    @PostMapping("/index-url")
    @ResponseBody
    public ResponseEntity<String> indexUrl(@RequestBody IndexedUrl indexedUrl) {
        try {
            String res = sv.indexar(indexedUrl.getUrl());
            System.out.println(res);
            if (res.equals("URL valido")) {
                return ResponseEntity.ok("URL adicionado com sucesso!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to index URL.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to index URL.");
        }
    }
    
    @GetMapping("/search")
    public String searchPage(@RequestParam String query, Model model) {
        if (query.length() < 1) {
            model.addAttribute("error", "Pesquisa inválida (1+ caracteres)");
            return "redirect:/dashboard?error=true";
        }
        
        model.addAttribute("query", query);
        return "search";
    }
    
    @PostMapping("/search")
    @ResponseBody
    public ResponseEntity<HashMap<String, ArrayList<String>>> searchResults(@RequestBody Map<String, String> query, @RequestParam int page) {
        try {
            String queryString = query.get("query");
            System.out.println("Pesquisando: " + queryString);
            HashMap<String, ArrayList<String>> results = sv.pesquisar(queryString);
            
            // Paginação
            int pageSize = 10;
            List<Map.Entry<String, ArrayList<String>>> resultList = new ArrayList<>(results.entrySet());
            int fromIndex = page * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, resultList.size());
            
            HashMap<String, ArrayList<String>> paginatedResults = new HashMap<>();
            for (int i = fromIndex; i < toIndex; i++) {
                Map.Entry<String, ArrayList<String>> entry = resultList.get(i);
                paginatedResults.put(entry.getKey(), entry.getValue());
            }
            
            return ResponseEntity.ok(paginatedResults);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}