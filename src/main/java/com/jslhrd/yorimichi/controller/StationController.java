package com.jslhrd.yorimichi.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequiredArgsConstructor
public class StationController {
    
    //private final StationService stationService
    
    //최신 리뷰를 가져오는 피드, count로 몇개 받아올 건지 정함
    @GetMapping("/feed")
    public void showFeed(@RequestBody int count) {
    }
    
}
