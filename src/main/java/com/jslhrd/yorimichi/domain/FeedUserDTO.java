package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class FeedUserDTO {
    private Long feedId; // FK → feed.feed_id
    private Long userId; // FK → user.user_id
}
