package com.jslhrd.yorimichi.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.domain.CoeatRequestDTO;
import com.jslhrd.yorimichi.service.CoeatService;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
public class CoeatController {
	
	private final CoeatService coeatService;

	//같이먹기 리스트
	@GetMapping("/coeats")
	public List<CoeatDTO> showCoeatList(){
		 return coeatService.getCoeatList();
	}

	//특정 가게 같이먹기 리스트
	@GetMapping("/coeats/{id}")
	public List<CoeatDTO> getMethodName(@PathVariable("id") Long storeId) {
		return coeatService.getCoeatListById(storeId);
	}


	//같이먹기 상세
	@GetMapping("/coeat/{id}")
	public CoeatDTO showCoeat(@PathVariable("id") Long coeatId) {
		return coeatService.getCoeatDetail(coeatId);
	}

	//같이먹기 작성
	@PostMapping("/coeat")
	public void postCoeat(@RequestBody CoeatDTO coeat, Principal principal) {
		//TODO: 유저id 찾아오는 커스텀 USER
		coeatService.save(coeat);
	}

	//같이먹기 수정
	@PutMapping("/coeat/{id}")
	public void putCoeat(@PathVariable("id") Long coeatId, @RequestBody CoeatDTO coeat, Principal principal) {
		//TODO: 유저id 찾아오는 커스텀 USER
		coeatService.updateCoeat(coeatId, coeat);
	}

	//같이먹기 삭제
	@DeleteMapping("/coeat/{id}")
	public void deleteCoeat(@PathVariable("id") Long coeatId, Principal principal){
		//TODO: 유저id 찾아오는 커스텀 USER
		coeatService.deleteCoeat(coeatId);
	}

	//같이먹기 신청
	@PostMapping("/coeat/{coeatId}/participant")
	public void participateCoeat(
		@PathVariable("coeatId") Long coeatId,
		@RequestBody CoeatRequestDTO coeatRequest,
		Principal principal) {
		long userId = 0;
		coeatService.joinCoeat(coeatId, coeatRequest, userId);
	}
	
	//같이먹기 수락
	//대기 상태에서 수락상태로 변경하는 거로 처리될 것 같아요
	@PatchMapping("/coeat/{coeatId}/participant/{participantId}")
	public void acceptParticipant(
		@PathVariable("coeatId") Long coeatId,
		@PathVariable("participantId") Long participantId,
		Principal principal){
		long userId = 0;
		coeatService.acceptParticipant(coeatId, participantId, userId);
	}

	//같이먹기 제외/거절/취소
	@DeleteMapping("/coeat/{coeatid}/participant")
	public void deleteParticipant(
		@PathVariable("coeatId") Long coeatId,
		@PathVariable("participantId") Long participantId,
		Principal principal){
		long userId = 0;
		coeatService.rejectParticipant(coeatId, participantId, userId);
	}
	

}
