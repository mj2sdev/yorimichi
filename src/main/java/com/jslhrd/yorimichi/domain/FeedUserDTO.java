package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 피드-유저 조인(참여/관계 등)
 * - (feed_id, user_id) 복합키 성격
 * - created_at 컬럼 없음
 */
@Getter
@Setter
public class FeedUserDTO {
    private Long feedId;    // FK → feed.id
    private Long userId;    // FK → user.id
}
