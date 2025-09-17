package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 루트-키워드 매핑
 * - (root_id, keyword_id) 복합키 성격
 */
@Getter
@Setter
public class RootKeywordDTO {
    private Long rootId;                // FK → root.id
    private Long keywordId;             // FK → keyword.id
    private LocalDateTime createdAt;    // 생성시각 (DB DEFAULT)
}
