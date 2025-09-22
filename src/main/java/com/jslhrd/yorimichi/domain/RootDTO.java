package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 루트 DTO.
 *
 * <br>루트 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
@Alias("RootDTO")
public class RootDTO  {

	/** PK: 루트 ID */
    private Long id;

	/** 생성일시 (DB 자동 생성) */
	private LocalDateTime createdAt;

	/** 수정일시 (DB 자동 갱신) */
	private LocalDateTime updatedAt;

	/** 삭제일시 */
    private LocalDateTime deletedAt;

	/** 숨김일시 */
    private LocalDateTime blindedAt;
}