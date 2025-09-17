package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 팔로우 관계
 * - follower_id: 나(팔로우 하는 사람)
 * - followee_id: 상대(팔로우 당하는 사람)
 * - notified: 알림 여부
 */
@Getter
@Setter
public class FollowDTO {
    private Long followerId;           // 팔로워 FK
    private Long followeeId;           // 팔로이 FK
    private Boolean notified;          // 알림 여부
    private LocalDateTime createdAt;   // 생성시각 (DB DEFAULT)
}
