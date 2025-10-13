package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.SocialAccountDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.enums.Provider;
import com.jslhrd.yorimichi.mapper.AccountMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * AccountService 구현체.
 * <p>
 * 트랜잭션 경계:
 * - 클래스 레벨 @Transactional: 기본적으로 쓰기 트랜잭션.
 * - 읽기 전용 메서드는 구현 시 @Transactional(readOnly=true)로 별도 최적화 가능.
 * <p>
 * 설계 포인트:
 * - 이메일은 trim + lower 정규화하여 저장/조회 일관 유지.
 * - 소셜 자격의 절대 키: (provider, providerUserId) UNIQUE 제약으로 멱등 보장.
 * - 신규 사용자 생성: root(USER) → user → (optional) social_account
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AccountManager implements AccountService {

	private final RootMapper rootMapper;
	private final AccountMapper accountMapper;
	private final PasswordEncoder passwordEncoder;

	private static String normEmail(String email) {
		return (email == null) ? null : email.trim().toLowerCase();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isNicknameAvailable(String nickname) {
		if (nickname == null || nickname.isBlank()) return false;
		return !accountMapper.existsNickname(nickname.trim());
	}

	/**
	 * 로컬 회원가입 (이메일 중복 확인 → root → user).
	 * - password는 반드시 BCrypt 해시로 저장
	 * - role은 USER 기본(DB role 테이블과 FK 일치)
	 */
	@Override
	public void signupLocal(UserDTO user) {
		// 1) 파라미터 필수값 검증
		String email = user.getEmail();
		String rawPassword = user.getPassword();
		String nickname = user.getNickname();
		if (email == null || rawPassword == null || nickname == null) {
			throw new IllegalArgumentException("email, password, nickname은 필수입니다.");
		}

		// 2) 이메일 정규화
		String normalizedEmail = normEmail(email);
		user.setEmail(normalizedEmail);

		// 3) 이메일 중복 확인
		if (accountMapper.selectByEmail(normalizedEmail).isPresent()) {
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
		}

		// 4) root(USER) 생성 → root PK가 user.id가 됨
		//    - user는 RootDTO를 상속한다고 가정 (id/type을 rootMapper.insert에서 세팅)
		rootMapper.insert(user); // useGeneratedKeys=true 필수

		// 5) 비밀번호 해시 저장
		user.setPassword(passwordEncoder.encode(rawPassword));

		// 6) account INSERT (role 기본값은 DB default 또는 별도 설정)
		accountMapper.insertUser(user);
	}

	/**
	 * 소셜 최초 로그인 시 계정 생성 or 기존 계정에 링크.
	 * <p>
	 * 흐름:
	 * 1) (provider, providerUserId)로 이미 링크된 유저가 있는지 조회
	 * 2) 없다면, providerEmail이 있으면 같은 이메일의 기존 유저를 찾아 소셜 계정만 링크
	 * 3) 이메일도 없거나 매칭 실패면 신규 root+user 생성 후 소셜 계정 링크
	 * <p>
	 * 멱등:
	 * - social_account(provider, provider_user_id) UNIQUE로 보장
	 */
	@Override
	public Long signupOrLinkSocial(SocialAccountDTO socialAccount) {
		// 0) 필수 키 검증
		Provider provider = socialAccount.getProvider();
		String providerUserId = socialAccount.getProviderUserId();
		if (provider == null || providerUserId == null || providerUserId.isBlank()) {
			throw new IllegalArgumentException("provider, providerUserId는 필수입니다.");
		}

		// 1) 이미 링크되어 있으면 그 userId 바로 반환
		Optional<UserDTO> findUser = accountMapper.selectUserByProviderAndSub(provider, providerUserId);
		if (findUser.isPresent()) {
			// 마지막 로그인 시각을 이 지점에서 갱신할 수 있음(옵션)

			Long userId = findUser.get().getId();
			accountMapper.updateLastLoginAt(userId);
			accountMapper.updateSocialLastLoginAt(userId, provider);
			return userId;
		}

		// 2) 이메일로 기존 유저 매칭 시도
		String email = normEmail(socialAccount.getProviderEmail());
		if (email != null && !email.isBlank()) {
			Optional<UserDTO> byEmail = accountMapper.selectByEmail(email);
			if (byEmail.isPresent()) {
				Long userId = byEmail.get().getId();
				socialAccount.setUserId(userId);
				// 2-1) 기존 유저에 소셜 계정 링크
				accountMapper.insertSocialAccount(
						/*provider,
						providerUserId,
						userId,
						email,
						socialAccount.isEmailVerified(),
						socialAccount.getDisplayName(),
						socialAccount.getAvatarUrl()*/
						socialAccount
				);
				// (선택) 마지막 로그인 갱신
				accountMapper.updateLastLoginAt(userId);
				accountMapper.updateSocialLastLoginAt(userId, provider);
				return userId;
			}
		}

		// 3) 신규 생성: root(USER) → user → social_account
		// 3-1) root 생성 (type='USER'), 생성된 PK를 userId로 사용
		UserDTO newUser = new UserDTO();
		// 필요 시 기본 닉네임/설명 등 초기값 세팅
		rootMapper.insert(newUser); // PK 채워짐
		Long userId = newUser.getId();

		// 3-2) user 생성 (소셜 전용: password NULL, role=USER 기본)
		//      닉네임: 공급자 displayName 있으면 사용, 없으면 "user{ID}"
		String displayName = socialAccount.getDisplayName();
		String nickname = (displayName != null && !displayName.isBlank()) ? displayName.trim() : "user" + userId;
		newUser.setId(userId);
		newUser.setEmail(email);            // 소셜 이메일 제공 시 저장 (nullable)
		newUser.setPassword(null);          // 소셜 가입이므로 비번 없음
		newUser.setNickname(nickname);
		// role은 DB default 또는 별도 세터/매퍼에서 처리
		accountMapper.insertUser(newUser);

		socialAccount.setUserId(userId);

		// 3-3) social_account 링크
		accountMapper.insertSocialAccount(
				/*provider,
				providerUserId,
				userId,
				email,
				socialAccount.isEmailVerified(),
				socialAccount.getDisplayName(),
				socialAccount.getAvatarUrl()*/
				socialAccount
		);

		// (선택) 마지막 로그인 갱신
		accountMapper.updateLastLoginAt(userId);
		accountMapper.updateSocialLastLoginAt(userId, provider);

		return userId;
	}

	/**
	 * 비밀번호 변경 (본인 인증 가정).
	 * - 필요 시 oldPassword 매칭 로직 추가 가능.
	 */
	@Override
	public void changePassword(Long userId, String oldPassword, String newPassword) {
		if (userId == null || newPassword == null || newPassword.isBlank()) {
			throw new IllegalArgumentException("userId와 newPassword는 필수입니다.");
		}
		// (선택) oldPassword 매칭 로직: accountMapper.selectPasswordHash(userId) 후 matches 검사
		String hash = passwordEncoder.encode(newPassword);
		int updated = accountMapper.updatePassword(userId, hash);
		if (updated != 1) {
			throw new IllegalStateException("비밀번호 변경 실패 (userId=" + userId + ")");
		}
	}

	/**
	 * 계정 삭제 (soft delete 권장).
	 * - root.deleted_at을 현재 시각으로 설정
	 */
	@Override
	public void deleteAccount(Long userId) {
		if (userId == null) throw new IllegalArgumentException("삭제할 사용자 ID가 필요합니다.");
		int updated = accountMapper.deleteUser(userId); // 구현체에서 soft delete 수행
		if (updated != 1) {
			throw new IllegalStateException("삭제(비활성) 실패: " + userId);
		}
	}

	@Override
	public void sendEmailVerification(String email) {
		// 토큰 생성 + 저장 + 메일 발송 (별도 EmailService 연동)
		// accountMapper.insertVerificationToken(email, token, expiresAt) ...
		throw new UnsupportedOperationException("TODO: 이메일 인증 발급 구현");
	}

	@Override
	public boolean confirmEmailVerification(String token) {
		// 토큰 검증 → 사용자/소셜 계정의 email_verified 반영
		// accountMapper.verifyEmailByToken(token) ...
		throw new UnsupportedOperationException("TODO: 이메일 인증 검증 구현");
	}
}