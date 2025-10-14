package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.SocialAccountDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.enums.Provider;
import com.jslhrd.yorimichi.exception.UserNotFoundException;
import com.jslhrd.yorimichi.mapper.AccountMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.service.AccountService;
import com.jslhrd.yorimichi.util.EmailNormalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.mapper.RoleMapper;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.service.AccountService;
import com.jslhrd.yorimichi.service.social.SocialAuthClient;
import com.jslhrd.yorimichi.service.social.SocialProfile;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountManager implements AccountService {

	private static final long DEFAULT_ROLE_ID = 1L; // TODO: 설정/조회로 대체

	private final UserMapper userMapper;
	private final AccountMapper accountMapper;
	private final EmailNormalizer emailNormalizer;
	private final RoleMapper roleMapper;
	private final PasswordEncoder passwordEncoder;
	private final SocialAuthClient socialAuthClient;

	/**
	 * 로컬 회원가입 (이메일 중복 확인 → root → user).
	 * - password는 반드시 BCrypt 해시로 저장
	 * - role은 USER 기본(DB role 테이블과 FK 일치)
	 */
	@Override
	public void signupLocal(UserDTO user) {

		String normalizedEmail = emailNormalizer.normalize(user.getEmail());
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
	 * 이메일 중복 검사 → 역할 유효성 검사 → 비밀번호 인코딩 → INSERT
	 * </p>
	 *
	 * @param user 사용자 등록 정보
	 */
	@Override
	public void signup(final UserDTO user) {
		if (userMapper.selectByEmail(user.getEmail()).isPresent()) {
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + user.getEmail());
		}

		// 역할 심어주는 곳이 없는데 여기서 역할 없다고 반려하면 안될 듯
		// 급하니까 역할 id 1(일반) 으로 고정하겠습니다.
		// roleMapper.selectById(user.getRoleId())
		// 		.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 역할 ID: " + user.getRoleId()));
		user.setRoleId(1l);

		// 2) 이메일로 기존 유저 매칭 시도
		String normalizedEmail = emailNormalizer.normalize(socialAccount.getProviderEmail());
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		int inserted = userMapper.insert(user);
		if (inserted != 1) {
			throw new IllegalStateException("회원 가입 실패");
		}
	}

	/**
	 * 비밀번호 변경.
	 *
	 * @param dto id와 새 비밀번호가 포함된 DTO
	 */
	@Override
	public void changePassword(final UserDTO dto) {
		if (dto.getId() == null || dto.getPassword() == null || dto.getPassword().isBlank()) {
			throw new IllegalArgumentException("비밀번호 변경에는 id와 password가 필요합니다.");
		}

		userMapper.selectById(dto.getId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자: " + dto.getId()));

		String encoded = passwordEncoder.encode(dto.getPassword());
		int updated = userMapper.updatePassword(dto.getId(), encoded);
		if (updated != 1) {
			throw new IllegalStateException("비밀번호 변경 실패");
		}
	}

	/**
	 * 회원 삭제.
	 *
	 * @param userId 삭제할 사용자 ID
	 */
	@Override
	public void delete(final Long userId) {
		if (userId == null)
			throw new IllegalArgumentException("삭제할 사용자 ID가 필요합니다.");
		if (userMapper.deleteById(userId) != 1) {
			throw new IllegalStateException("삭제 실패: " + userId);
		}
	}

	/**
	 * 소셜 토큰 기반 회원 가입.
	 *
	 * @param token 소셜 인증 토큰
	 */
	@Override
	public void signupSocial(final String token) {
		if (token == null || token.isBlank()) {
			throw new IllegalArgumentException("소셜 토큰이 필요합니다.");
		}

		SocialProfile profile = socialAuthClient.verify(token);
		if (profile == null || profile.email() == null || profile.email().isBlank()) {
			throw new IllegalArgumentException("소셜 프로필에 이메일이 없습니다.");
		}

		if (userMapper.selectByEmail(profile.email()).isPresent()) {
			throw new IllegalArgumentException("이미 가입된 이메일입니다: " + profile.email());
		}

		roleMapper.selectById(DEFAULT_ROLE_ID)
				.orElseThrow(() -> new IllegalStateException("기본 역할이 존재하지 않습니다: " + DEFAULT_ROLE_ID));

		// 임시 비밀번호 생성 후 인코딩
		String rawTemp = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
		String encodedTemp = passwordEncoder.encode(rawTemp);

		UserDTO dto = new UserDTO();
		dto.setRoleId(DEFAULT_ROLE_ID);
		dto.setEmail(profile.email());
		dto.setPassword(encodedTemp);
		dto.setNickname(profile.nickname() != null ? profile.nickname() : "user");

		int inserted = userMapper.insert(dto);
		if (inserted != 1) {
			throw new IllegalStateException("소셜 회원가입 실패");
		}
	}

	@Override
	public boolean validateNickname(String nickname) {
		return !userMapper.existsNickname(nickname);
	}

	@Override
	public boolean verificateEmail(String email) {
		return userMapper.selectByEmail(email).isEmpty();
	}
}
