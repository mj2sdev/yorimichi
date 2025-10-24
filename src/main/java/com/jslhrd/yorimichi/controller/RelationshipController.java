package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.RelationshipService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class RelationshipController {

	private final RelationshipService relationshipService;

	//사용자가 팔로우 한 리스트
	@GetMapping("/follow")
	public List<UserDTO> showFollow(@AuthenticationPrincipal(expression = "userId") Long userId) {
		return relationshipService.findFollowees(userId);
	}

	//사용자가 팔로우 당한 리스트
	@GetMapping("/follower")
	public List<UserDTO> showFollower(@AuthenticationPrincipal(expression = "userId") Long userId) {
		return relationshipService.findFollowers(userId);
	}

	//사용자가 상대 팔로우
	@PostMapping("/follow/{targetId}")
	public void followUser(@AuthenticationPrincipal(expression = "userId") Long userId, @PathVariable("targetId") Long targetId) {
		relationshipService.saveFollow(userId, targetId);
	}

	//사용자가 상대 언팔로우
	@DeleteMapping("/follow/{targetId}")
	public void unfollowUser(@AuthenticationPrincipal(expression = "userId") Long userId, @PathVariable("targetId") Long targetId) {
		relationshipService.deleteFollow(userId, targetId);
	}

	//사용자가 차단한 대상 목록
	@GetMapping("/block")
	public List<UserDTO> shoeBlockUser(@AuthenticationPrincipal(expression = "userId") Long userId) {
		return relationshipService.findBlocks(userId);
	}


	//사용자가 상대를 차단
	@PostMapping("/block/{targetId}")
	public void blockuser(@AuthenticationPrincipal(expression = "userId") Long userId, @PathVariable("targetId") Long targetId) {
		relationshipService.saveBlock(userId, targetId);
	}

	//사용자가 상대를 차단해제
	@DeleteMapping("/block/{targetId}")
	public void unblockuser(@AuthenticationPrincipal(expression = "userId") Long userId, @PathVariable("targetId") Long targetId) {
		relationshipService.deleteBlock(userId, targetId);
	}
}
