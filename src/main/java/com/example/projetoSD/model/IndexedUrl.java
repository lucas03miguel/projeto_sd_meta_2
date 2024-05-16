package com.example.projetoSD.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class IndexedUrl implements Serializable {
    /**
     * URL da página web.
     */
    private final String url;
    /**
     * Título da página web.
     */
    private String title;
    /**
     * Citação da página web.
     */
    private String textSnippet;
    /**
     * Palavras da página web.
     */
    private final Set<String> words;
    
    /**
     * Construtor da classe WebPage.
     *
     * @param url URL da página web
     * @param title título da página web
     * @param textSnippet citação da página web
     * @param words palavras da página web
     */
    public IndexedUrl(String url, String title, String textSnippet, Set<String> words) {
        this.url = url.replaceAll("[\n;|]+", "");
        this.title = title.replaceAll("[\n;|]+", "");
        this.textSnippet = textSnippet.replaceAll("[\n;|]+", "");
        Set<String> wordsCopy = new HashSet<>();
        for (String word : words) {
            word = word.replaceAll("[\n;|]+", "");
            wordsCopy.add(word);
        }
        this.words = wordsCopy;
    }
    
    /**
     * Obtém o URL da página web.
     *
     * @return URL da página web
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * Obtém o título da página web.
     *
     * @return título da página web
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Define o título da página web.
     *
     * @param title título da página web
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Obtém a citação da página web.
     *
     * @return citação da página web
     */
    public String getTextSnippet() {
        return textSnippet;
    }
    
    /**
     * Define a citação da página web.
     *
     * @param textSnippet citação da página web
     */
    public void setTextSnippet(String textSnippet) {
        this.textSnippet = textSnippet;
    }
    
    /**
     * Obtém as palavras da página web.
     *
     * @return as palavras da página web
     */
    public Set<String> getWords() {
        return words;
    }
}
