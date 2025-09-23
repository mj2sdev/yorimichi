package com.jslhrd.yorimichi.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jslhrd.yorimichi.domain.NotificationDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
//@RequiredArgsConstructor
public class NotificationController {
    
    //private final NotificationService notificationService;

    @GetMapping("/notification")
    public void getNotifications() {
        //TODO: 사용자의 id를 찾아옴
        //찾아온 id를 바탕으로 서비스에서 관련 알림을 가져옴.
        //return NotificationDTO notification = notificationService.method();
    }
    
}
