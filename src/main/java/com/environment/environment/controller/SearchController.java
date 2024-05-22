package com.environment.environment.controller;

import com.environment.environment.dto.SearchResultDTO;
import com.environment.environment.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public SearchResultDTO search(@RequestParam String query) {
        return searchService.search(query);
    }
}