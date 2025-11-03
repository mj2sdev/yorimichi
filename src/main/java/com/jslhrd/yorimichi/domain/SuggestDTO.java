package com.jslhrd.yorimichi.domain;

import lombok.Data;

@Data
public class SuggestDTO {
    private String type;    // STORE or REGION
    private Long id;
    private String label;   // 목록에 크게 노출
    private String sublabel;// 보조(구/군, 카테고리 등)
    private String path;    // 클릭 이동 경로
}