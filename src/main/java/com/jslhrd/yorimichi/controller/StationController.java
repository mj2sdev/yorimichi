package com.jslhrd.yorimichi.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jslhrd.yorimichi.domain.UserDTO;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequiredArgsConstructor
public class StationController {
    
    private final RelationshipController relationshipController;
    //private final StationService stationService

    //정류장으로 이동
	@GetMapping("/station")
	public String showStation(Principal principal, Model model) {
        List<UserDTO> followList = relationshipController.showFollow(principal);
        List<UserDTO> followerList = relationshipController.showFollower(principal);
        //List<FeedDTO> feeds = stationService.();

        model.addAttribute("followlist",followList);
        model.addAttribute("followerList", followerList);
        //model.addAttribute("feeds", feeds);
		//같이먹기나와 리뷰의 리스트를 가져올 수 있어야함
		return "station/list";
	}
    
    //최신 리뷰를 가져오는 피드, count로 몇개 받아올 건지 정함
    @GetMapping("/feed")
    public void showFeed(@RequestParam(value = "count") int count) {
        //List<?> feedList = stationService.();
    }
    
}
