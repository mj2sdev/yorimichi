package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserBlockDTO {
    private Long blockerId;             // FK → user.user_id (차단한 유저)
    private Long blockedId;             // FK → user.user_id (차단당한 유저)
    private LocalDateTime createdAt;    // CURRENT_TIMESTAMP
}
