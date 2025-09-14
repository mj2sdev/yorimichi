package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class KeywordDTO {
    private Long keywordId;
    private String name;
    private LocalDateTime createdAt;
}
