package com.jslhrd.yorimichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/store")
public class StoreController {

    
    @GetMapping("/list")
    public String toListForTest() {
        return "/store/list";
    }
    @GetMapping("/view")
    public String toViewforTest() {
        return "/store/view";
    }
    
    
}
