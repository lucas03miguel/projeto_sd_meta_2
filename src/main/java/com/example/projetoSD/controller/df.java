/*
package com.example.projetoSD.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.projetoSD.model.IndexedUrl;
import com.example.projetoSD.service.IndexedUrlService;

@Controller
public class SearchController {
    
    @Autowired
    private IndexedUrlService indexedUrlService;
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        model.addAttribute("results", indexedUrlService.search(query));
        return "index";
    }
    
    @GetMapping("/addUrl")
    public String addUrl(@RequestParam("url") String url, @RequestParam("title") String title, @RequestParam("snippet") String snippet) {
        IndexedUrl indexedUrl = new IndexedUrl(url, title, snippet);
        indexedUrl.setUrl(url);
        indexedUrl.setTitle(title);
        indexedUrl.setSnippet(snippet);
        indexedUrlService.addIndexedUrl(indexedUrl);
        return "redirect:/";
    }
}
*/
