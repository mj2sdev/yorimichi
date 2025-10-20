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
	 * 유저 단건 조회.
	 *
	 * @param userId 유저 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<UserDTO> selectById(@Param("userId") Long userId);

	/**
	 * 닉네임 존재 여부 (XML에서 SELECT EXISTS(...)로 구현 권장)
	 */
	boolean existsByNickname(@Param("nickname") String nickname);

	boolean existsActive(@Param("userId") Long userId);

	/**
	 * user INSERT (로컬/소셜 공용).
	 * 전제: rootMapper.insert(user)로 user.id가 미리 채워져 있어야 함(useGeneratedKeys=true).
	 * 주의: 소셜 가입 시 password는 NULL 허용.
	 */
	int insert(UserDTO user);

	/**
	 * 유저 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(@Param("userId") Long userId, @Param("user") UserDTO user);

	/**
	 * 사용자 비활성(soft delete) 또는 삭제.
	 * 실제 구현이 root.deleted_at 업데이트라면 XML/Javadoc에 명시.
	 * 영향 행수 반환
	 */
	int deleteById(@Param("userId") Long userId);
}