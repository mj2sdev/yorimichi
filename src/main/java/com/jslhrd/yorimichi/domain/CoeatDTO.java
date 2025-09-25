package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 같이먹기 DTO.
 * <p>
 * 같이먹기 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
@Alias("CoeatDTO")
public class CoeatDTO extends RootDTO {

	/**
	 * FK: 유저 ID
	 */
	private Long userId;

	/**
	 * FK: 상점 ID
	 */
	private Long storeId;

	/**
	 * 같이먹기 제목
	 */
	private String title;

	/**
	 * 같이먹기 내용
	 */
	private String content;

	/**
	 * 같이먹기 정원
	 */
	private Integer capacity;

	/**
	 * 같이먹기 조회수
	 */
	private Integer viewCount;

	/**
	 * 같이먹기 모임일시
	 */
	private LocalDateTime meetingAt;

	/**
	 * 같이먹기 상태
	 */
	private String status;

	/**
	 * 자동 수락 여부
	 */
	private Boolean autoAccept;
}