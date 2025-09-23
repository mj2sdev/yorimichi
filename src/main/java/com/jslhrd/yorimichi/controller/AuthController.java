package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.UserDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class AuthController {
    
    //로그아웃
    @PostMapping("/logout")
    public String logout() {
        return "redirect:/index.html";
    }

    @ResponseBody
    @GetMapping("/signup/nickname")
    public boolean validateNickname(@RequestBody String nickname) {
        boolean result = false;

        return result;
    }

    @ResponseBody
    @PostMapping("/signup/email")
    public boolean postMethodName(@RequestBody String email) {
        boolean result = false;

        return result;
    }
    
    @PostMapping("/login")
    public String login(@RequestBody UserDTO user) {

        return "redirect:/index.html";
    }

    @PostMapping("/login/social")
    public String socialLogin(){
        
        return "redirect:/index.html";
    }
    
    


    
    
}
