package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
public class RootDTO  {
    
    private Long rootId;               // BIGINT AI, PK
    private LocalDateTime createdAt;   // DEFAULT CURRENT_TIMESTAMP
    private LocalDateTime updatedAt;   // DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    private LocalDateTime deletedAt;   // NULL (삭제일시)
    private LocalDateTime blindedAt;   // NULL (검열일시)
}
