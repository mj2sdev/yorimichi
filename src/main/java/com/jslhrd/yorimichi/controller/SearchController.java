// src/main/java/com/jslhrd/yorimichi/controller/SearchController.java
package com.jslhrd.yorimichi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jslhrd.yorimichi.domain.SuggestDTO;
import com.jslhrd.yorimichi.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping(value="/_ping", produces="text/plain;charset=UTF-8")
    public String ping(){ return "ok"; }

@GetMapping(value="/suggest", produces="application/json;charset=UTF-8")
public List<SuggestDTO> suggest(@RequestParam("q") String q,
                                @RequestParam(value="limit", defaultValue="10") int limit){
    return searchService.suggest(q.trim(), limit);
}



}

