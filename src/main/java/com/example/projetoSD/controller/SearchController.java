package com.example.projetoSD.controller;

import com.example.projetoSD.interfaces.RMIServerInterface;
import com.example.projetoSD.model.IndexedUrl;
import com.example.projetoSD.service.IndexedUrlService;
import com.example.projetoSD.service.UrlFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String indexUrl(@RequestBody IndexedUrl indexedUrl) {
        try {
            sv.indexar(indexedUrl.getUrl());
            return "URL successfully indexed.";
        } catch (Exception e) {
            return "Failed to index URL.";
        }
    }
}
