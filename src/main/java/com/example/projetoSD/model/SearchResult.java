package com.example.projetoSD.model;


public class SearchResult {
    private String word;
    private int occurrences;
    
    public SearchResult() {
    }
    
    public SearchResult(String word, int occurrences) {
        this.word = word;
        this.occurrences = occurrences;
    }
    
    public String getWord() {
        return word;
    }
    
    public void setWord(String word) {
        this.word = word;
    }
    
    public int getOccurrences() {
        return occurrences;
    }
    
    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }
    
    public String getQuery() {
        return word;
    }
}

