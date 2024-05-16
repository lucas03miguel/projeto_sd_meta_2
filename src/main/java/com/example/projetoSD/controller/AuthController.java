package com.example.projetoSD.controller;

import com.example.projetoSD.model.IndexedUrl;
import com.example.projetoSD.model.User;
import com.example.projetoSD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AuthController {
    
    private final UserService userService;
    
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/")
    public RedirectView redirectToLogin() {
        return new RedirectView("/login");
    }
    
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("formRequest", new User());
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@ModelAttribute("formRequest") User formRequest, Model model) {
        if (userService.validateLogin(formRequest.getUsername(), formRequest.getPassword())) {
            return "redirect:/dashboard"; // Login bem-sucedido, redirecionar para a página principal
        }
        model.addAttribute("error", "Invalid username or password");
        return "redirect:/login?error=true";
    }
    
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("formRequest", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@ModelAttribute("formRequest") User formRequest, Model model) {
        if (userService.registerUser(formRequest)) {
            return "redirect:/dashboard"; // Registro bem-sucedido, redirecionar para a página de login
        }
        model.addAttribute("error", "Username already exists");
        return "redirect:/login?error=true";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        //model.addAttribute("indexedUrls", userService.getIndexedUrls());
        return "dashboard";
    }
    
}