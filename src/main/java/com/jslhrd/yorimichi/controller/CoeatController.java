package com.jslhrd.yorimichi.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jslhrd.yorimichi.domain.CoeatDTO;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
public class CoeatController {
    
    //같이먹기 상세
    @GetMapping("/coeat/{id}")
    public void getCoeat() {
        //return CoeatDTO coeat;
    }
    
    //같이먹기 작성
    @PostMapping("/coeat")
    public void postCoeat(@RequestBody CoeatDTO coeat) {
        //return List<CoeatDTO> coeats;
    }
    
    //같이먹기 수정
    @PutMapping("/coeat")
    public void putCoeat(@RequestBody CoeatDTO coeat) {
    }

    //같이먹기 삭제
    @DeleteMapping("/coeat")
    public void deleteCoeat(@RequestBody Long id){
        //return List<CoeatDTO> coeats;
    }
    
    //같이먹기 신청
    @PostMapping("/coeat/participant")
    public void participateCoeat(@RequestBody Long coeatId) {
        //return List<CoeatRequestDTO> participants;
    }
    
    //같이먹기 수락
    //대기 상태에서 수락상태로 변경하는 거로 처리될 것 같아요
    @PatchMapping("/coeat/participant")
    public void acceptParticipant(@RequestBody Long UserId){
        //return List<CoeatRequestDTO> participants;
    }

    //같이먹기 제외/거절
    //명단에서 제거하는 건데, 상태 변경 없이 삭제로 다 될 것 같아요
    @DeleteMapping("/coeat/participant")
    public void deleteParticipant(@RequestBody Long UserId){
        //return List<CoeatRequestDTO> participants;
    }
    

}
