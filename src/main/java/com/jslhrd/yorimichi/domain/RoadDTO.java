package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 도로명 DTO.
 *
 * <br>도로명 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
@Alias("RoadDTO")
public class RoadDTO {

	/** PK: 도로명 ID */
    private Long id;

	/** FK: 읍/면/동 ID */
    private Long emdId;
	
	/** 도로명 코드 */
    private String code;
	
	/** 도로명 이름 */
    private String name;

	/** 생성일시 (DB 자동 생성) */
	private LocalDateTime createdAt;

	/** 수정일시 (DB 자동 갱신) */
	private LocalDateTime updatedAt;
}