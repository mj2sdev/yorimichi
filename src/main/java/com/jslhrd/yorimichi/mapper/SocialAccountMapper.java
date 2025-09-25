package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.SocialAccountDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 소셜 계정 Mapper.
 *
 * <br>소셜 계정 정보를 전달합니다.
 *
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface SocialAccountMapper {

	/**
	 * 소셜 계정 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(SocialAccountDTO dto);

	/**
	 * 소셜 계정 단건 조회.
	 *
	 * @param userId   소셜 계정 유저 ID
	 * @param provider 소셜 계정 제공자
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<SocialAccountDTO> selectByUserIdAndProvider(@Param("userId") Long userId,
	                                                     @Param("provider") String provider);

	/**
	 * 소셜 계정 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(SocialAccountDTO dto);

	/**
	 * 소셜 계정 삭제.
	 *
	 * @param userId   소셜 계정 유저 ID
	 * @param provider 소셜 계정 제공자
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteByUserIdAndProvider(@Param("userId") Long userId,
	                              @Param("provider") String provider);
}