package com.jslhrd.yorimichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/station")
public class StationController {

    @GetMapping("/list")
    public void toList () {
    }
    
    
}
