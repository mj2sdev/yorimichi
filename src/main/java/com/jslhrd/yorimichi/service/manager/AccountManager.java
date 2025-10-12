package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.mapper.AccountMapper;
import com.jslhrd.yorimichi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountManager implements AccountService {

	private final AccountMapper accountMapper;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 소셜 최초 로그인 시 계정 생성 or 기존 계정에 링크.
	 * - (provider, sub)로 먼저 조회
	 * - 없으면 email로 기존 user 매칭 → 있으면 그 user에 social_account만 INSERT
	 * - email도 없거나 매칭 실패면 새 root+user 생성 후 social_account INSERT
	 * <p>
	 * 멱등 보장:
	 * - social_account(provider, provider_user_id) UNIQUE 제약으로 중복 링크 방지
	 */
	@Override
	public Long signupOrLinkSocial(String provider, String sub,
	                               String email, boolean verified,
	                               String name, String picture) {

		// 1) 이미 링크되어 있으면 그 userId 바로 반환
		var linked = accountMapper.selectByProviderAndSub(provider, sub);
		if (linked.isPresent()) {
			return linked.get().getId();
		}

		Long userId;

		// 2) 이메일이 제공되었다면 동일 이메일 유저가 있는지 확인
		if (email != null && !email.isBlank()) {
			var byEmail = accountMapper.selectByEmail(email.trim().toLowerCase());
			if (byEmail.isPresent()) {
				// 2-1) 기존 로컬/소셜 유저에 소셜 계정만 링크
				userId = byEmail.get().getId();
				accountMapper.insertSocialAccount(provider, sub, userId, email, verified, name, picture);
				// (선택) 마지막 로그인 갱신
				accountMapper.updateLastLoginAt(userId);
				accountMapper.updateSocialLastLoginAt(userId, provider);
				return userId;
			}
		}

		// 3) 신규 생성: root(USER) → user → social_account
		// 3-1) root 생성 (type='USER'), 생성된 PK를 userId로 사용
		Long newRootId = accountMapper.insertRootForUser(); // useGeneratedKeys=true 필요
		userId = newRootId;

		// 3-2) user 생성 (소셜 전용: password NULL, role=USER 기본)
		String nickname = (name != null && !name.isBlank()) ? name : "user" + userId;
		accountMapper.insertSocialUser(userId, email, nickname);

		// 3-3) social_account 링크
		accountMapper.insertSocialAccount(provider, sub, userId, email, verified, name, picture);

		// (선택) 마지막 로그인 갱신
		accountMapper.updateLastLoginAt(userId);
		accountMapper.updateSocialLastLoginAt(userId, provider);

		return userId;
	}

	/**
	 * 로컬 회원가입 (이메일 중복 확인 → root → user).
	 * - password는 반드시 BCrypt 해시로 저장
	 * - role은 USER 기본(DB role 테이블과 FK 일치)
	 */
	@Override
	public Long signupLocal(String email, String rawPassword, String nickname) {
		if (email == null || rawPassword == null || nickname == null) {
			throw new IllegalArgumentException("email, password, nickname은 필수입니다.");
		}
		String normEmail = email.trim().toLowerCase();

		if (accountMapper.selectByEmail(normEmail).isPresent()) {
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + normEmail);
		}

		Long userId = accountMapper.insertRootForUser();
		String hash = passwordEncoder.encode(rawPassword);
		accountMapper.insertLocalUser(userId, normEmail, hash, nickname);

		return userId;
	}

	/**
	 * 비밀번호 변경 (본인 인증 가정).
	 * - 여기서는 oldPassword 검증은 호출자가 끝냈다고 가정(필요시 추가).
	 */
	@Override
	public void changePassword(Long userId, String oldPassword, String newPassword) {
		if (userId == null || newPassword == null || newPassword.isBlank()) {
			throw new IllegalArgumentException("userId와 newPassword는 필수입니다.");
		}
		// (선택) oldPassword 매칭 로직이 필요하면 accountMapper.selectPasswordHash(userId) 후 matches 검사
		String hash = passwordEncoder.encode(newPassword);
		int updated = accountMapper.updatePassword(userId, hash);
		if (updated != 1) throw new IllegalStateException("비밀번호 변경 실패");
	}

	/**
	 * 계정 삭제 (soft delete 권장).
	 * - root.deleted_at을 현재 시각으로 설정
	 */
	@Override
	public void deleteAccount(Long userId) {
		if (userId == null) throw new IllegalArgumentException("삭제할 사용자 ID가 필요합니다.");
		int updated = accountMapper.deleteUser(userId);
		if (updated != 1) throw new IllegalStateException("삭제(비활성) 실패: " + userId);
	}

	@Override
	public boolean isNicknameAvailable(String nickname) {
		if (nickname == null || nickname.isBlank()) return false;
		return accountMapper.countByNickname(nickname) == 0;
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