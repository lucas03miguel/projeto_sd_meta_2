package com.example.projetoSD.web;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class Teste {
    
    @GetMapping("/")
    public String index(Model m) {
        m.addAttribute("IndexRequest", new IndexRequest());
        return "index";
    }
    
    @GetMapping("/login")
    public String login(Model m) {
        m.addAttribute("FormRequest", new FormRequest());
        return "login";
    }

}
