package com.example.projetoSD.controller;


import com.example.projetoSD.model.User;
import com.example.projetoSD.service.UserService;
import com.example.projetoSD.web.FormRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
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
    public String loginForm(Model m) {
        m.addAttribute("FormRequest", new User());
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@ModelAttribute User u, Model model) {
        String username = u.getUsername();
        String password = u.getPassword();
        
        if (userService.validateLogin(username, password))
            return "redirect:/";
        
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }
    
    @GetMapping("/register")
    public String registerForm(Model m) {
        m.addAttribute("RegisterRequest", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@ModelAttribute User u, Model model) {
        String username = u.getUsername();
        String password = u.getPassword();
        
        User newUser = new User(username, password);
        if (userService.registerUser(newUser))
            return "redirect:/login"; // Registro bem-sucedido, redirecionar para a página de login
        
        model.addAttribute("error", "Username already exists");
        return "register";
    }
}
