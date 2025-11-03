package com.jslhrd.yorimichi.enums;

import lombok.Getter;

@Getter
public enum Day {
	SUNDAY("일", 0),
	MONDAY("월", 1),
	TUESDAY("화", 2),
	WEDNESDAY("수", 3),
	THURSDAY("목", 4),
	FRIDAY("금", 5),
	SATURDAY("토", 6);

	private final String koreanName;

	private final Integer value;

	Day(String koreanName, Integer value) {
		this.koreanName = koreanName;
		this.value = value;
	}
}
