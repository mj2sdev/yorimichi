package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 루트-이미지 매핑
 * - (root_id, image_id) 복합키 성격
 */
@Getter
@Setter
public class RootImageDTO {
    private Long rootId;                // FK → root.id
    private Long imageId;               // FK → image.id
    private LocalDateTime createdAt;    // 생성시각 (DB DEFAULT)
}
