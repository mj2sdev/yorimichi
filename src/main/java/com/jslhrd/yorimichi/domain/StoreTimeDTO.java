package com.jslhrd.yorimichi.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreTimeDTO {
	private Long storeId;
	private DayOfWeek dayOfWeek;
	private LocalTime openTime;
	private LocalTime closeTime;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
