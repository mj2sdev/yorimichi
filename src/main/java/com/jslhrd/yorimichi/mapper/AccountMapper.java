package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface AccountMapper {

	/**
	 * 이메일 정규화(소문자/trim)는 호출부에서 보장
	 */
	Optional<UserDTO> selectByEmail(@Param("email") String email);

	/**
	 * 비밀번호 변경(BCrypt 해시 저장). 영향 행수 반환
	 */
	int updatePassword(@Param("userId") Long userId,
	                   @Param("hashedPassword") String hashedPassword);

	/**
	 * 마지막 로그인(공통) 타임스탬프 갱신.
	 * 영향 행수 반환(성공 시 1, 대상 없음이면 0)
	 */
	int updateLastLoginAt(@Param("userId") Long userId);
}