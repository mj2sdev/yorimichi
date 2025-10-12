package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.AccountDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface AccountMapper {

	// --- 인증 전용 "읽기" (최소 컬럼만) ---
	Optional<AccountDTO> selectByEmail(@Param("email") String email);

	Optional<AccountDTO> selectByProviderAndSub(@Param("provider") String provider,
	                                            @Param("providerUserId") String providerUserId);

	int countByNickname(@Param("nickname") String nickname);

	// --- 계정/소셜 "쓰기" (최초 소셜 로그인 등) ---
	Long insertRootForUser();

	void insertLocalUser(@Param("userId") Long userId,
	                     @Param("normEmail") String normEmail,
	                     @Param("hash") String hash,
	                     @Param("nickname") String nickname);

	void insertSocialUser(@Param("userId") Long userId,
	                      @Param("email") String email,
	                      @Param("nickname") String nickname);

	void insertSocialAccount(@Param("provider") String provider,
	                         @Param("sub") String sub,
	                         @Param("userId") Long userId,
	                         @Param("email") String email,
	                         @Param("verified") boolean verified,
	                         @Param("name") String name,
	                         @Param("picture") String picture);

	void updateLastLoginAt(@Param("userId") Long userId);

	void updateSocialLastLoginAt(@Param("userId") Long userId, @Param("provider") String provider);

	int updatePassword(@Param("userId") Long userId, @Param("hash") String hash);

	int deleteUser(@Param("userId") Long userId);
}
