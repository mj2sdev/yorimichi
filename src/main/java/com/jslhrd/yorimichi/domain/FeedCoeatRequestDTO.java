package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FeedCoeatRequestDTO {
	private Long feedId;          // FK
	private Long userId;          // FK
	private String status;
	private String message;
	private LocalDateTime createdAt;
}
