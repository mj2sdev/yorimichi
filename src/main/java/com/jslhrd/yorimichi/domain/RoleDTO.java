package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 권한/역할 DTO.
 *
 * <br>권한/역할 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class RoleDTO {

	/** PK: 권한/역할 ID */
    private Long id;     // PK

	/** 권한/역할 이름 */
    private String name; // 역할명

	/** 생성일시 (DB 자동 생성) */
	private LocalDateTime createdAt;

	/** 수정일시 (DB 자동 갱신) */
	private LocalDateTime updatedAt;
}