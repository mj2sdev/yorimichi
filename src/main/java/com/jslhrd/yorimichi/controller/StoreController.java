package com.jslhrd.yorimichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("/store")
public class StoreController {

    //가게 상세정보로 이동
    @GetMapping("/detail/{id}")
    public String showDetail(){
        return "/store/detail";
    }

    @GetMapping("/list")
    public String toListForTest() {
        return "/store/list";
    }

    @GetMapping("/detail")
    public String toViewforTest() {
        return "/store/detail";
    }
    
    
}
