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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

	//같이먹기 상세
	//	"/coeat?coeatId={feedid}"
	@GetMapping("/coeat")
	public CoeatDTO showCoeat(@RequestParam(value = "coeatId") Long feedId) {
		return coeatService.getCoeatDetail(feedId);
	}

	//같이먹기 삭제
	//	"/coeat?coeatId={feedid}"
	@DeleteMapping("/coeat")
	public void deleteCoeat(@RequestParam(value = "coeatId") Long feedId){
		coeatService.deleteCoeat(feedId);
	}

	//같이먹기 작성
	@PostMapping("/coeat")
	public void postCoeat(@RequestBody CoeatDTO coeat) {
		coeatService.save(coeat);
	}
	
	//같이먹기 수정
	@PutMapping("/coeat")
	public void putCoeat(@RequestBody CoeatDTO coeat) {
		coeatService.updateCoeat(coeat);
	}

	//같이먹기 신청
	@PostMapping("/coeat/participant")
	public void participateCoeat(@RequestBody CoeatDTO coeat,Principal principal) {
		coeatService.joinCoeat(null, null);
		//TODO: 현재 userId를 알아낼 방법이 없습니다.
	}
	
	//같이먹기 수락
	//대기 상태에서 수락상태로 변경하는 거로 처리될 것 같아요
	@PatchMapping("/coeat/participant")
	public void acceptParticipant(@RequestBody CoeatRequestDTO coeatRequest, Principal principal){
		coeatService.acceptParticipant(principal, coeatRequest);
	}

	//같이먹기 제외/거절/취소
	@DeleteMapping("/coeat/participant")
	public void deleteParticipant(@RequestBody CoeatRequestDTO coeatRequest, Principal principal){
		coeatService.rejectParticipant(principal, coeatRequest);
	}
	

}
