package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.SocialAccountDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.enums.Provider;
import com.jslhrd.yorimichi.exception.UserNotFoundException;
import com.jslhrd.yorimichi.mapper.AccountMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountManager implements AccountService {

	private final RootMapper rootMapper;
	private final UserMapper userMapper;
	private final AccountMapper accountMapper;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 로컬 회원가입 (이메일 중복 확인 → root → user).
	 * - password는 반드시 BCrypt 해시로 저장
	 * - role은 USER 기본(DB role 테이블과 FK 일치)
	 */
	@Override
	public void signupLocal(UserDTO user) {

		String normalizedEmail = normalizeEmail(user.getEmail());
		String rawPassword = user.getPassword();

		if (userMapper.selectByEmail(normalizedEmail).isPresent()) {
			throw new IllegalStateException("이미 가입된 이메일입니다.");
		}

		if (userMapper.existsNickname(user.getNickname())) {
			throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
		}

		user.setEmail(normalizedEmail);
		rootMapper.insert(user);

		Long userId = user.getId();
		if (userId == null) {
			throw new IllegalStateException("Root: insert failed or no generated userId");
		}

		user.setPassword(passwordEncoder.encode(rawPassword));
		accountMapper.insertLocalAccount(user);

		// 정책: 이메일 인증 메일 발송(옵션)
		// emailVerificationService.issueAndSend(email, rootId);
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
	public Long signupSocial(SocialAccountDTO socialAccount) {

		Provider provider = socialAccount.getProvider();
		String providerUserId = socialAccount.getProviderUserId();

		// 1) 이미 링크되어 있으면 그 userId 바로 반환
		Optional<UserDTO> findSocialAccount = accountMapper.selectByProviderAndSub(provider, providerUserId);
		if (findSocialAccount.isPresent()) {

			// 마지막 로그인 시각을 이 지점에서 갱신할 수 있음(옵션)
			Long userId = findSocialAccount.get().getId();
			accountMapper.updateLastLoginAt(userId);
			accountMapper.updateSocialLastLoginAt(userId, provider);

			return userId;
		}


		// 2) 이메일로 기존 유저 매칭 시도
		String normalizedEmail = normalizeEmail(socialAccount.getProviderEmail());

		Optional<UserDTO> findUser = userMapper.selectByEmail(normalizedEmail);
		if (findUser.isPresent()) {

			// 2-1) 기존 유저에 소셜 계정 링크
			Long userId = findUser.get().getId();
			socialAccount.setUserId(userId);

			accountMapper.insertSocialAccount(socialAccount);

			// (선택) 마지막 로그인 갱신
			accountMapper.updateLastLoginAt(userId);
			accountMapper.updateSocialLastLoginAt(userId, provider);

			return userId;
		}


		// 3) 신규 생성
		UserDTO newUser = new UserDTO();

		rootMapper.insert(newUser);

		Long userId = newUser.getId();
		if (userId == null) {
			throw new IllegalStateException("Root: insert failed or no generated userId");
		}

		// 닉네임: 공급자 displayName 있으면 사용, 없으면 "user{ID}"
		String displayName = socialAccount.getDisplayName();
		String nickname = (displayName != null && !displayName.isBlank()) ? displayName.trim() : "user" + userId;

		// 3-2) user 생성
		newUser.setId(userId);
		newUser.setEmail(normalizedEmail); // 소셜 이메일 제공 시 저장 (nullable)
		newUser.setPassword(null);         // 소셜 가입이므로 비번 없음
		newUser.setNickname(nickname);
		accountMapper.insertLocalAccount(newUser);

		// 3-2) social_account 링크
		socialAccount.setUserId(userId);
		accountMapper.insertSocialAccount(socialAccount);

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

		userMapper.selectById(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));

		if (!passwordEncoder.matches(oldPassword, newPassword)) {
			throw new SecurityException("기존 비밀번호가 일치하지 않습니다.");
		}

		String hashed = passwordEncoder.encode(newPassword);
		boolean affected = accountMapper.updatePassword(userId, hashed) > 0;
		if (!affected) {
			throw new IllegalStateException("비밀번호 변경 실패 (userId=" + userId + ")");
		}

		log.info("Account: updated password userId={}", userId);
	}

	/**
	 * 계정 삭제 (soft delete 권장).
	 * - root.deleted_at을 현재 시각으로 설정
	 */
	@Override
	public void delete(Long userId) {

		boolean affected = accountMapper.deleteById(userId) > 0;
		if (!affected) {
			boolean exists = userMapper.existsActive(userId);
			if (!exists) {
				throw new IllegalStateException("삭제(비활성) 실패: " + userId);
			}
			log.debug("Account: delete no-op userId={}", userId);
			return;
		}

		log.info("Account: soft deleted userId={}", userId);
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

	private String normalizeEmail(String rawEmail) {
		if (rawEmail == null) {
			throw new IllegalArgumentException("email은 필수입니다.");
		}
		return rawEmail.trim().toLowerCase();
	}
}