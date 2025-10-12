package com.jslhrd.yorimichi.service;

/**
 * 계정관련 서비스
 *
 * @author mj2sdev
 * @author mj2sdev
 * @version 1.1 {@code UserDetails} 삭제
 * <p>
 * 실제 필요했던건 {@code UserDetailsService}
 * 해당 인터페이스는 {@code UserService} 이쪽으로 옮김
 */
public interface AccountService {

	/**
	 * 소셜 최초 로그인 시 계정 생성 or 기존 계정에 링크.
	 * - SocialUserService.loadUser(...)에서 호출됨.
	 * - 멱등: (provider, sub) UNIQUE 제약으로 중복 링크 방지.
	 *
	 * @return userId (생성되었거나 링크된 사용자 ID)
	 */
	Long signupOrLinkSocial(String provider, String sub,
	                        String email, boolean emailVerified,
	                        String displayName, String avatarUrl);

	/**
	 * 로컬 계정 가입.
	 * - 내부에서 root(USER) 생성 → user INSERT → (필요 시) 초기 role 부여.
	 * - password는 반드시 BCrypt 등으로 해시되어 저장.
	 */
	Long signupLocal(String email, String rawPassword, String nickname);

	/**
	 * 비밀번호 변경(본인 인증 후).
	 * - oldPassword 검증 → newPassword 해시 저장.
	 */
	void changePassword(Long userId, String oldPassword, String newPassword);

	/**
	 * 계정 삭제(또는 비활성/탈퇴 처리).
	 * - 보통 root.deleted_at 세팅으로 soft delete를 수행.
	 */
	void deleteAccount(Long userId);

	/**
	 * 닉네임 중복 검사.
	 */
	boolean isNicknameAvailable(String nickname);

	/**
	 * 이메일 인증 토큰 발급(전송) 트리거.
	 * - 토큰 저장 및 메일 발송을 수행.
	 */
	void sendEmailVerification(String email);

	/**
	 * 이메일 인증 토큰 검증(확정).
	 * - 토큰 검증 성공 시 user/social_account의 email_verified 반영.
	 */
	boolean confirmEmailVerification(String token);
}
