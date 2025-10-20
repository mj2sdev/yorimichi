package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.SocialAccountDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.enums.Provider;
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

	Optional<UserDTO> selectByProviderAndProviderUserId(@Param("provider") Provider provider,
	                                                    @Param("providerUserId") String providerUserId);

	/**
	 * 소셜 계정 단건 조회.
	 *
	 * @param userId   소셜 계정 유저 ID
	 * @param provider 소셜 계정 제공자
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<SocialAccountDTO> selectByUserIdAndProvider(@Param("userId") Long userId,
	                                                     @Param("provider") Provider provider);

	/**
	 * social_account 링크 INSERT.
	 * 전제: socialAccount.userId가 세팅되어 있어야 함.
	 * 매핑: providerEmail → provider_email, displayName → display_name 등
	 */
	int insert(SocialAccountDTO socialAccount);

	/**
	 * 소셜별 마지막 로그인 타임스탬프 갱신.
	 * 영향 행수 반환(성공 시 1, 대상 없음이면 0)
	 */
	int updateLastLoginAt(@Param("userId") Long userId,
	                      @Param("provider") Provider provider);

	/**
	 * 소셜 계정 삭제.
	 *
	 * @param userId   소셜 계정 유저 ID
	 * @param provider 소셜 계정 제공자
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int delete(@Param("userId") Long userId,
	           @Param("provider") Provider provider);
}