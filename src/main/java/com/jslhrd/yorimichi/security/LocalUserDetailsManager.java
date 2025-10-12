package com.jslhrd.yorimichi.security;

import com.jslhrd.yorimichi.domain.AccountDTO;
import com.jslhrd.yorimichi.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 폼 로그인(로컬)에서 호출되는 사용자 로딩 서비스.
 * - DaoAuthenticationProvider가 email(username)로 호출합니다.
 * - 인증에 필요한 최소 컬럼만 조회(성능/보안상 권장).
 * - 조회 결과를 AppUserPrincipal로 변환하여 반환합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocalUserDetailsManager implements UserDetailsService {

	private final AccountMapper accountMapper;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		final String normEmail = (email == null) ? null : email.toLowerCase();

		// 1) 이메일로 인증 최소 정보 조회 (user/password/role/root.deleted_at/blinded_at)
		AccountDTO account = accountMapper.selectByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("No user for email: " + normEmail));

		// 2) UserDTO → AppUserPrincipal(슬림) 변환
		var principal = AppUserPrincipal.fromLocal(account);

		log.debug("Local login user loaded: id={}, email={}", principal.getUserId(), principal.getEmail());
		return principal;
	}
}