package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.SearchResultDTO;
import com.jslhrd.yorimichi.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SearchPageController {

    private final SearchService searchService;

    // /search?q=상점01&type=store&id=2001
    @GetMapping("/search")
    public String searchPage(
            @RequestParam(value="q",required = false) String q,
            @RequestParam(value="type",required = false) String type,
            @RequestParam(value="id",required = false) Long id,
            @RequestParam(value="page",defaultValue = "1") int page,
            @RequestParam(value="size",defaultValue = "10") int size,
            Model model
    ) {
        SearchResultDTO result = searchService.search(q, type, id, page, size);
        model.addAttribute("result", result);
        return "search/list"; // templates/search/list.html
    }
}
