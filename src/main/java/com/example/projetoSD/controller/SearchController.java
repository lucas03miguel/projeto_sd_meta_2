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
        model.addAttribute("query", query);
        return "search";
    }
    
    @PostMapping("/search")
    @ResponseBody
    public ResponseEntity<HashMap<String, ArrayList<String>>> search(@RequestBody Map<String, String> query, @RequestParam int page) {
        try {
            String queryString = query.get("query");
            if (queryString.length() < 1) {
                HashMap<String, ArrayList<String>> error = new HashMap<>();
                ArrayList<String> errorList = new ArrayList<>();
                errorList.add("Pesquisa inválida (1+ caracteres)");
                error.put("error", errorList);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            System.out.println("Pesquisando: " + queryString);
            HashMap<String, ArrayList<String>> results = sv.pesquisar(queryString);
        
            // Paginação
            int pageSize = 10;
            List<Map.Entry<String, ArrayList<String>>> resultList = new ArrayList<>(results.entrySet());
            int fromIndex = page * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, resultList.size());
            System.out.println("aqui");
        
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
