package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class FeedCoeatDTO {
    private Long feedCoeatId;     // feed_coeat.id
    private Long feedId;          // FK
    private Long storeId;         // FK
    private Integer capacity;
    private LocalDateTime meetingAt;
    private Boolean autoAccept;
    private String status;
}
