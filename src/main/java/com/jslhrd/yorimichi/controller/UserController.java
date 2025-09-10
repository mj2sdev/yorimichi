package com.jslhrd.yorimichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @GetMapping("/signup")
    public String toSignup() {
        return "user/signup";
    }

    @GetMapping("/login")
    public String toLogin(){
        return "user/login";
    }
    
}
