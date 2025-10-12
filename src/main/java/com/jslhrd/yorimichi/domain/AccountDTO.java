package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.RoleName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 앱 '사용자'의 공통 최소 정보 (응답/조회용)
 * - 비밀번호/소셜 자격 없음 (누출 방지)
 * - 활성/잠금 여부는 deletedAt/blindedAt로 계산 가능
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO implements Serializable {

	/**
	 * user.id (= root.id)
	 */
	private Long id;
	/**
	 * user.email (로그인 아이디)
	 */
	private String email;
	/**
	 * user.password (로그인 비밀번호)
	 */
	private String password;
	/**
	 * user.nickname
	 */
	private String nickname;
	/**
	 * 역할(Enum) - DB의 ENUM('USER','ADMIN')과 1:1
	 */
	private RoleName role;

	/**
	 * 탈퇴/비활성 시각 (null이면 활성)
	 */
	private LocalDateTime deletedAt;

	/**
	 * 블라인드/잠금 시각 (null이면 비잠금)
	 */
	private LocalDateTime blindedAt;

	/**
	 * 마지막 로그인 시각(선택)
	 */
	private LocalDateTime lastLoginAt;

	/**
	 * 편의 헬퍼
	 */
	public boolean isEnabled() {
		return deletedAt == null;
	}

	public boolean isNonLocked() {
		return blindedAt == null;
	}
}
