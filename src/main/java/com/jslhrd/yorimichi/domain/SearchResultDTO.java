package com.jslhrd.yorimichi.domain;

import java.util.Collections;
import java.util.List;
import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class SearchResultDTO {
    // 요청 파라미터 에코
    private String q;      // 사용자가 입력한 키워드
    private String type;   // store / region 등 (지금은 store만 사용)
    private Long   id;     // 선택 항목 id (자동완성 클릭 등)

    // 페이징
    private int page;      // 1-base
    private int size;      // 페이지 크기
    private long total;    // 총 건수

    // 결과(지금은 상점만)
    private List<StoreDTO> stores;
    

    /** 목록(키워드 검색 결과) */
     @Builder.Default
    private List<StoreDTO> items = Collections.emptyList();

    /* --------------------- 추가된 필드들 --------------------- */

    /** 자동완성 클릭: 우측 큰 영역에 보여줄 단건 상점 */
    private StoreDTO store;

    /** 자동완성 클릭: 좌측 필터 영역에 보여줄 상점의 카테고리 목록 */
     @Builder.Default
    private List<CategoryDTO> categories = Collections.emptyList();
}
