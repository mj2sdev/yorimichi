package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

	/**
	 * 유저 단건 조회.
	 *
	 * @param userId 유저 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<UserDTO> selectById(@Param("userId") Long userId);

	Optional<UserDTO> selectByEmail(@Param("email") String email);

	boolean existsActive(@Param("userId") Long userId);

	/**
	 * 유저 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(UserDTO user);

	/**
	 * 유저 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(UserDTO user);

	int updatePassword(@Param("userId") Long userId,
	                   @Param("password") String password);

	/**
	 * 유저 삭제.
	 *
	 * @param userId 유저 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("userId") Long userId);
}