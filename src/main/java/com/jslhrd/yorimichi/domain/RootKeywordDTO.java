package com.jslhrd.yorimichi.domain;

import java.time.LocalDateTime;
import lombok.*;


@Getter
@Setter
public class RootKeywordDTO {
    private Long rootId;  
    private Long keywordId;
    private LocalDateTime createdAt; 
}
