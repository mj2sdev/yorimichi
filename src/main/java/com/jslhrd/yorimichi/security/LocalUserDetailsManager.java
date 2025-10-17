package com.jslhrd.yorimichi.security;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.util.EmailNormalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LocalUserDetailsManager
 * -----------------------
 * 폼 로그인(로컬)에서 DaoAuthenticationProvider가 username(email)로 호출하는 사용자 로딩 서비스입니다.
 * <p>
 * 설계 의도:
 * - DB에서 인증에 필요한 **최소 컬럼만** 조회하여 성능과 보안을 동시에 확보합니다.
 * (예: password hash, role, 상태 플래그를 유도하는 deletedAt/blindedAt 등)
 * - 조회된 UserDTO를 **슬림 Principal**(AppUserPrincipal)로 변환해 SecurityContext에 탑재합니다.
 * <p>
 * 보안/운영 포인트:
 * - 이메일은 **trim + NFKC 정규화 + lower-case**로 표준화하여 중복/미스매치 방지.
 * - 존재하지 않는 계정에 대해 구체적인 에러 메시지를 노출하지 않고,
 * {@link UsernameNotFoundException}에 일반화된 문구를 사용하여 **유저 열거 공격**을 완화합니다.
 * - 본 메서드는 읽기 전용 트랜잭션으로, 불필요한 쓰기 잠금을 방지합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LocalUserDetailsManager implements UserDetailsService {

	/**
	 * 인증 전용 최소 조회를 담당하는 매퍼(이메일 → UserDTO with password/role/state).
	 */
	private final UserMapper userMapper;
	private final EmailNormalizer emailNormalizer;

	/**
	 * DaoAuthenticationProvider가 호출하는 진입점.
	 * <p>
	 * 흐름:
	 * 0) 입력 값 방어 (null/empty)
	 * 1) 이메일 정규화 (trim + NFKC + lower)
	 * 2) DB에서 인증 최소 정보 조회 (없으면 일반화된 예외로 유저 열거 방지)
	 * 3) UserDTO → AppUserPrincipal 변환 (enabled/nonLocked 등은 RootDTO 파생값)
	 * 4) debug 로그 (민감정보 제외)
	 *
	 * @param email 로그인 폼에서 입력된 이메일(= username)
	 * @return AppUserPrincipal (UserDetails 구현체)
	 * @throws UsernameNotFoundException 존재하지 않거나 비정상 입력일 때
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		// 0) 널/공백 입력 방어
		// 외부(클라이언트)로는 "계정이 없거나 비밀번호가 틀렸다" 수준의 일반화된 메시지를 반환하여
		// 특정 계정의 존재 여부를 유추하기 어렵게 합니다.
		if (email == null || email.isEmpty()) {
			throw new UsernameNotFoundException("Invalid credentials");
		}

		// 1) 이메일 정규화: trim + NFKC + lower
		final String normEmail = emailNormalizer.normalize(email);

		// 2) 이메일 기준으로 인증 최소 정보 조회
		//  - userMapper.selectByEmail은 Optional<UserDTO>를 반환(단건 관례)
		//  - 실패 시 외부 메시지는 일반화, 내부 로그(debug)로만 상세 원인 남김
		UserDTO user = userMapper.selectByEmail(normEmail)
				.orElseThrow(() -> {
					if (log.isDebugEnabled()) {
						log.debug("No user found for email: {}", normEmail);
					}
					// "사용자 없음"을 직접 노출하지 않고 일반화된 메시지로 통일
					return new UsernameNotFoundException("Bad credentials");
				});

		// 3) DTO → 슬림 프린시펄 변환
		//  - AppUserPrincipal.fromLocal은 role null 방어 및 enabled/nonLocked 파생을 수행
		//  - password는 반드시 해시 전제(BCrypt 등); 평문 금지
		var principal = AppUserPrincipal.fromLocal(user);

		// 4) 성공 로그 (민감정보 제외)
		if (log.isDebugEnabled()) {
			log.debug("Local login user loaded: id={}, email={}", principal.getUserId(), principal.getEmail());
		}

		return principal;
	}
}