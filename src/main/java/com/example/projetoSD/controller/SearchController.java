package com.example.projetoSD.controller;

import com.example.projetoSD.interfaces.RMIServerInterface;
import com.example.projetoSD.model.IndexedUrl;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String indexUrl(@RequestBody IndexedUrl indexedUrl) {
        if (isValidUrl(indexedUrl.getUrl())) {
            try {
                sv.indexar(indexedUrl.getUrl());
                return "URL successfully indexed.";
            } catch (Exception e) {
                return "Failed to index URL.";
            }
        } else {
            return "Invalid URL.";
        }
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
