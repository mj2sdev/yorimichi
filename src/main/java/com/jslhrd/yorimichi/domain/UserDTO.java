package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {
    private Long userId;          // PK
    private Long roleId;          // 권한 FK
    private String email;         // 이메일
    private String password;      // 비밀번호(해시 저장 가정)
    private String nickname;      // 닉네임
    private String description;   // 소개 (NULL 가능)
    private LocalDateTime lastLoginAt; // 마지막 로그인 일시 (NULL 가능)
}
