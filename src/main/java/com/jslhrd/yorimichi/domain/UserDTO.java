package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 사용자
 */
@Getter
@Setter
public class UserDTO {
    private Long id;                 // PK
    private Long roleId;             // FK → role.id
    private String email;            // 이메일
    private String password;         // 패스워드(해시 저장)
    private String nickname;         // 닉네임
    private String description;      // 소개 (NULL 허용)
    private LocalDateTime lastLoginAt; // 마지막 로그인 시각 (NULL 허용)
}
