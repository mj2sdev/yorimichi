package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.RoleName;
import com.jslhrd.yorimichi.validation.Create;
import com.jslhrd.yorimichi.validation.Update;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
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

	/**
	 * PK: 권한/역할 ID
	 */
	@Null(groups = Create.class)
	@NotNull(groups = Update.class)
	private Long id;

	/**
	 * 권한/역할 이름
	 */
	@NotNull
	private RoleName name;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	@PastOrPresent
	@Null(groups = Create.class)
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	@PastOrPresent
	@Null(groups = Create.class)
	private LocalDateTime updatedAt;
}