package com.example.projetoSD.service;

import java.util.List;

import com.example.projetoSD.model.IndexedUrl;
import org.springframework.stereotype.Service;

@Service
public class IndexedUrlService {
    private final FileService fileService = new FileService();
    private List<IndexedUrl> indexedUrls;
    
    public IndexedUrlService() {
        this.indexedUrls = fileService.loadIndexedUrls();
    }
    
    public List<IndexedUrl> getAllIndexedUrls() {
        return indexedUrls;
    }
    
    public void addIndexedUrl(IndexedUrl url) {
        indexedUrls.add(url);
        fileService.saveIndexedUrls(indexedUrls);
    }
    
    public List<IndexedUrl> search(String query) {
        // Implementar a lógica de busca
        return null;
    }
}

