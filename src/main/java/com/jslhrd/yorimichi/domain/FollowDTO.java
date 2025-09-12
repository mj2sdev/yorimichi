package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class FollowDTO {
    private Long followerId;            // FK → user.user_id (팔로우한 유저)
    private Long followeeId;            // FK → user.user_id (팔로우받은 유저)
    private Boolean notified;           // BOOLEAN (기본 TRUE)
    private LocalDateTime createdAt;    // CURRENT_TIMESTAMP
}
