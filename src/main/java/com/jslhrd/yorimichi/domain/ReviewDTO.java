package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class ReviewDTO {
    private Long reviewId;
    private Long userId;
    private Long foodId;
    private Integer rating; // 1~5 등 범위는 나중에 검증에서 처리
    private String content;    
}
