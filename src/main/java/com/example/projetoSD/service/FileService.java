package com.example.projetoSD.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.example.projetoSD.model.IndexedUrl;

public class FileService {
    private static final String FILE_PATH = "../database/indexed_urls.dat";
    
    public void saveIndexedUrls(List<IndexedUrl> urls) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(urls);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<IndexedUrl> loadIndexedUrls() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<IndexedUrl>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
