package com.example.projetoSD.service;

import com.example.projetoSD.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserFileService userFileService = new UserFileService();
    private List<User> users;
    
    public UserService() {
        this.users = userFileService.loadUsers();
    }
    
    public List<User> getAllUsers() {
        return users;
    }
    
    public boolean registerUser(User user) {
        if (findUserByUsername(user.getUsername()) != null)
            return false;
        
        users.add(user);
        userFileService.saveUsers(users);
        return true;
    }
    
    public User findUserByUsername(String username) {
        for (User user : users)
            if (user.getUsername().equals(username))
                return user;
        return null;
    }
    
    public boolean validateLogin(String username, String password) {
        User user = findUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }
}

