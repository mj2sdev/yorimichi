package com.jslhrd.yorimichi.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequiredArgsConstructor
public class StationController {
    
    //private final StationService stationService

    //정류장으로 이동
	@GetMapping("/station")
	public String showStation() {
		//사용자의 id를 바탕으로 팔로우, 팔로워 등을 가져올 수 있어야 함
		//같이먹기나와 리뷰의 리스트를 가져올 수 있어야함
		return "station/list";
	}
    
    //최신 리뷰를 가져오는 피드, count로 몇개 받아올 건지 정함
    @GetMapping("/feed")
    public void showFeed(@RequestBody int count) {
    }
    
}
