package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.SocialAccountDTO;
import com.jslhrd.yorimichi.domain.UserDTO;

/**
 * 계정 관련 도메인 서비스 (트랜잭션 경계는 구현체에서 선언).
 *
 * <h2>역할</h2>
 * <ul>
 *   <li>소셜(OAuth2/OIDC) 로그인 최초 진입 시 <b>멱등 프로비저닝</b>(link/signup)</li>
 *   <li>로컬 가입/비밀번호 변경/탈퇴 등 사용자 라이프사이클 관리</li>
 *   <li>이메일 인증(토큰 발급/검증)의 트리거 및 반영</li>
 * </ul>
 *
 * <h2>일반 규칙</h2>
 * <ul>
 *   <li>이메일은 반드시 <b>trim + NFKC + lower-case</b> 정규화하여 저장/조회한다.</li>
 *   <li>비밀번호는 반드시 <b>BCrypt 등 단방향 해시</b>로 저장하며 평문 저장 금지.</li>
 *   <li>소셜 자격의 절대 키는 <b>(provider, providerUserId)</b>이며 UNIQUE 제약으로 멱등 보장.</li>
 *   <li>예외는 도메인 별 커스텀 예외(예: DuplicateNicknameException, BadCredentialsException 등)를 구현체에서 던질 수 있다.</li>
 * </ul>
 *
 * @author mj2sdev
 * @version 1.1 {@code UserDetails} 삭제 (실제 필요는 {@code UserDetailsService})
 */
public interface AccountService {

	/**
	 * 소셜 <b>최초 로그인</b> 시 계정 생성 또는 기존 계정에 링크한다(멱등).
	 * <p>
	 * 구현 예:
	 * <ol>
	 *   <li>(provider, providerUserId)로 social_account 조회</li>
	 *   <li>없으면 root(USER) → user → social_account 순서로 생성 (동시에 UNIQUE 제약으로 멱등 보장)</li>
	 *   <li>이미 존재하면 링크 재생성 없이 해당 userId 반환</li>
	 * </ol>
	 *
	 * <h3>사이드 이펙트</h3>
	 * <ul>
	 *   <li>최초 가입 시 닉네임/프로필 초기화 전략 적용 가능</li>
	 *   <li>로그인 이력 업데이트는 트래픽 규모에 따라 비동기로 분리 권장</li>
	 * </ul>
	 *
	 * @param socialAccount provider / providerUserId / email(옵션) / displayName(옵션) 등
	 * @return userId (생성되었거나 이미 링크된 사용자 ID)
	 * @throws IllegalArgumentException 필수 키 누락 시 (provider, providerUserId)
	 */
	Long signupOrLinkSocial(SocialAccountDTO socialAccount);

	/**
	 * 로컬 계정 가입.
	 * <p>
	 * 구현 예:
	 * <ol>
	 *   <li>email 정규화 및 중복 검사(LOWER(email) UNIQUE)</li>
	 *   <li>password 해시(BCrypt)</li>
	 *   <li>root(USER) → user INSERT → 초기 role 부여</li>
	 * </ol>
	 *
	 * @param user 이메일/비밀번호(해시 전 or 후 여부는 구현 합의), 닉네임 등
	 * @throws IllegalArgumentException 필수 값 누락/형식 오류
	 * @throws IllegalStateException    이메일 중복 등 정책 위반
	 */
	void signupLocal(UserDTO user);

	/**
	 * 비밀번호 변경(본인 인증 후).
	 * <p>
	 * 구현 예:
	 * <ol>
	 *   <li>oldPassword 검증(BCrypt matches)</li>
	 *   <li>newPassword 정책 검사(길이/복잡도)</li>
	 *   <li>newPassword 해시 저장</li>
	 * </ol>
	 *
	 * @param userId      대상 사용자 ID
	 * @param oldPassword 기존 평문 비밀번호
	 * @param newPassword 새 평문 비밀번호
	 * @throws IllegalArgumentException 입력 누락/정책 위반
	 * @throws SecurityException        기존 비밀번호 불일치
	 */
	void changePassword(Long userId, String oldPassword, String newPassword);

	/**
	 * 계정 삭제(또는 비활성/탈퇴 처리).
	 * <p>
	 * 일반적으로 <b>soft delete</b> 전략: root.deleted_at 세팅.
	 * 필요한 경우 관련 자원(토큰/세션/알림 구독) 정리.
	 *
	 * @param userId 대상 사용자 ID
	 */
	void deleteAccount(Long userId);

	/**
	 * 닉네임 중복 검사.
	 *
	 * @param nickname 검사할 닉네임(정규화/트리밍은 구현체에서 처리)
	 * @return 사용 가능하면 true, 아니면 false
	 */
	boolean isNicknameAvailable(String nickname);

	/**
	 * 이메일 인증 토큰 발급(전송) 트리거.
	 * <p>
	 * <b>권장</b>: 발급/전송에 <b>레이트리밋</b>과 <b>멱등</b>을 적용.
	 * 동일 이메일에 대해 최근 유효 토큰이 있으면 재사용하거나 재발급 간격 제한.
	 *
	 * @param email 대상 이메일(정규화 필요)
	 */
	void sendEmailVerification(String email);

	/**
	 * 이메일 인증 토큰 검증(확정).
	 * <p>
	 * 성공 시 user 또는 social_account의 email_verified 반영.
	 * 토큰 일회성/만료/취소 정책 명확화 필요.
	 *
	 * @param token 이메일 인증 토큰
	 * @return 검증 성공 시 true, 실패/만료 시 false
	 */
	boolean confirmEmailVerification(String token);
}