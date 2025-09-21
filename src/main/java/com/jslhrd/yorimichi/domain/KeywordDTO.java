package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 키워드 DTO.
 *
 * <br>키워드 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
@Alias("KeywordDTO")
public class KeywordDTO {

	/** PK: 키워드 ID */
    private Long id;

	/** 키워드 이름 */
    private String name;

	/** 생성일시 (DB 자동 생성) */
	private LocalDateTime createdAt;

	/** 수정일시 (DB 자동 갱신) */
	private LocalDateTime updatedAt;
}