package com.example.projetoSD.controller;

import com.example.projetoSD.interfaces.RMIServerInterface;
import com.example.projetoSD.model.IndexedUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;

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

}
