package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter

public class FeedDTO {
    /** PK이자 root(root_id)를 참조하는 FK */
    private Long feedId;
    private Long userId;
    private String title;
    private String content;
    private String status;
    private Integer viewCount;
}
