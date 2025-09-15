package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Aggregate Root 메타
 */
@Getter
@Setter
public class RootDTO {
    private Long id;                    // PK
    private LocalDateTime createdAt;    // 생성시각 (DB DEFAULT)
    private LocalDateTime updatedAt;    // 수정시각
    private LocalDateTime deletedAt;    // 삭제일시 (NULL 허용)
    private LocalDateTime blindedAt;    // 블라인드 일시 (NULL 허용)
}
