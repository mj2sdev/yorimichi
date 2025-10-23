package com.jslhrd.yorimichi.domain.response;

import com.jslhrd.yorimichi.enums.CoeatStatus;
import com.jslhrd.yorimichi.enums.RootType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StationDTO {

	private Long id;
	private RootType type;
	private LocalDateTime createdAt;

	private Long storeId;
	private String name;
	private Integer rating;
	private String content;

	private String title;
	private String meetingAt;
	private CoeatStatus status;
	private Integer capacity;
}