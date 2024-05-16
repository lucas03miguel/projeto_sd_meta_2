package com.example.projetoSD.service;


import com.example.projetoSD.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserFileService {
    private static final String FILE_PATH = "./src/main/java/com/example/projetoSD/database/users.dat";
    
    public void saveUsers(List<User> users) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                boolean __ = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<User> loadUsers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                boolean __ = file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file");
                e.printStackTrace();
            }
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}

