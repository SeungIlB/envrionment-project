package com.environment.environment.service;

import java.util.List;

public class SearchResult {
    private List<Item> items;

    public static class Item {
        private String title;
        private String link;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getLink() { return link; }
        public void setLink(String link) { this.link = link; }
    }

    // Getters and Setters
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}