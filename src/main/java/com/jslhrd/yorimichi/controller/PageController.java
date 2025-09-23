package com.jslhrd.yorimichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;




@Controller
public class PageController {

    //인덱스로 이동
    @GetMapping("/index")
    public String showIndex() {
        return "/index";
    }

    //정류장으로 이동
    @GetMapping("/station")
    public String showStation() {
        return "/station/list";
    }

    //회원가입 페이지로 이동
    @GetMapping("/signup")
    public String showSignup(){
        return "/user/signup";
    }
    
    //로그인 화면으로 이동
    @GetMapping("/login")
    public String showLogin(){
        return "/user/login";
    }

}
