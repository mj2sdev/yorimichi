package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 유저 Mapper.
 *
 * <br>유저 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface UserMapper {

	List<UserDTO> selectAll(SearchDTO search);

	/**
	 * 이메일 정규화(소문자/trim)는 호출부에서 보장
	 */
	Optional<UserDTO> selectByEmail(@Param("email") String email);

	/**
	 * 유저 단건 조회.
	 *
	 * @param userId 유저 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<UserDTO> selectById(@Param("userId") Long userId);

	/**
	 * 닉네임 존재 여부 (XML에서 SELECT EXISTS(...)로 구현 권장)
	 */
	boolean existsNickname(@Param("nickname") String nickname);

	boolean existsActive(@Param("userId") Long userId);

	/**
	 * 유저 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(@Param("userId") Long userId, @Param("user") UserDTO user);
}