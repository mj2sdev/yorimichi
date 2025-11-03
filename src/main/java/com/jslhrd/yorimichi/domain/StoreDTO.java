package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StoreDTO {
    private Long id;
    private String name;
    private String description;
    private String phone;

    // 주소 FK + 중첩 주소객체
    private Long addressId;
    private AddressDTO address;

    // 화면용 보조 필드들 (있으면 편함)
    private String thumbnailUrl;   // 썸네일 경로 매핑 예정
    private Integer reviewCount;   // 리뷰 수(없으면 null 허용)

    // (선택) 카테고리 태그를 가게에 물릴 계획이면
    private List<CategoryDTO> categories;
}
