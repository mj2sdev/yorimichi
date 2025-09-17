package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 리뷰
 * - ERD: id, user_id, food_id, rating, content
 * - created_at 컬럼 없음
 */
@Getter
@Setter
public class ReviewDTO {
    private Long id;        // PK
    private Long userId;    // FK → user.id
    private Long foodId;    // FK → food.id
    private Integer rating; // 평점 (예: 1~5)
    private String content; // 내용
}
