package com.jslhrd.yorimichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/member")
public class MemberController {
    
    @GetMapping("/signup")
    public String toSignup() {
        return "member/signup";
    }

    @GetMapping("/login")
    public String toLogin(){
        return "member/login";
    }
    
}
