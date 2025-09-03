package com.jslhrd.yorimichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class MainController {

    @GetMapping("/")
    public String helloYorimichi(Model model) {
        model.addAttribute("message", "Hello, Yorimichi!");
        return "index";
    }
}
