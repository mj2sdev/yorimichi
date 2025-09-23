package com.jslhrd.yorimichi.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class RelationshipController {
    

    //내가 상대 팔로우
    @PostMapping("/follow")
    public void followUser(@RequestBody Long userId) {
    }

    //내가 상대 언팔로우
    @DeleteMapping("/follow")
    public void unfollowUser(@RequestBody Long userId) {
    }
    
    //나를 팔로우한 사람을 강제 취소
    @DeleteMapping("/follower")
    public void unfollowFromMe(@RequestBody Long userId) {
    }
    
    //내가 상대를 차단
    @PostMapping("/block")
    public void blockuser(@RequestBody Long userId) {
    }

    //내가 상대를 차단해제
    @DeleteMapping("/block")
    public void unblockuser(@RequestBody Long userId) {
    }
}
