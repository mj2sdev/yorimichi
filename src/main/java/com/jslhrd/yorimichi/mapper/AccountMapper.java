package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.SocialAccountDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.enums.Provider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface AccountMapper {

	/**
	 * social_account(provider, provider_user_id) → user 조인 조회
	 */
	Optional<UserDTO> selectByProviderAndSub(@Param("provider") Provider provider,
	                                         @Param("providerUserId") String providerUserId);

	/**
	 * user INSERT (로컬/소셜 공용).
	 * 전제: rootMapper.insert(user)로 user.id가 미리 채워져 있어야 함(useGeneratedKeys=true).
	 * 주의: 소셜 가입 시 password는 NULL 허용.
	 */
	int insertLocalAccount(UserDTO user);

	/**
	 * social_account 링크 INSERT.
	 * 전제: socialAccount.userId가 세팅되어 있어야 함.
	 * 매핑: providerEmail → provider_email, displayName → display_name 등
	 */
	int insertSocialAccount(SocialAccountDTO socialAccount);

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

	/**
	 * 소셜별 마지막 로그인 타임스탬프 갱신.
	 * 영향 행수 반환(성공 시 1, 대상 없음이면 0)
	 */
	int updateSocialLastLoginAt(@Param("userId") Long userId,
	                            @Param("provider") Provider provider);

	/**
	 * 사용자 비활성(soft delete) 또는 삭제.
	 * 실제 구현이 root.deleted_at 업데이트라면 XML/Javadoc에 명시.
	 * 영향 행수 반환
	 */
	int deleteById(@Param("userId") Long userId);
}