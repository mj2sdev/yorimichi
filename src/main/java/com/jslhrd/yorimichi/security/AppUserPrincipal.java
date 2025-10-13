package com.jslhrd.yorimichi.security;

import com.jslhrd.yorimichi.domain.SocialAccountDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.enums.Role;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 인증 후 SecurityContext에 저장될 "애플리케이션 사용자" 프린시펄.
 * <p>
 * - 슬림 전략: 세션/직렬화 부담을 최소화하기 위해 인증·인가에 필요한 필드만 보유합니다.
 * (OIDC id_token / claims 같은 큰 객체는 세션에 싣지 않음)
 * <p>
 * - 로컬 로그인(폼): UserDetails 경로
 * - 소셜 로그인(OAuth2/OIDC): OAuth2User 경로
 * → 두 경로 모두 동일 타입(AppUserPrincipal)로 컨트롤러/서비스에 주입되므로
 *
 * @AuthenticationPrincipal 사용이 일관됩니다.
 * <p>
 * 구현 포인트
 * 1) equals/hashCode는 userId 기준(@EqualsAndHashCode(of="userId"))
 * 2) toString에서 password 제외(@ToString(exclude="password"))
 * 3) 상태 플래그는 RootDTO의 deletedAt/blindedAt을 기반으로 계산해 세션에는 boolean만 보관
 */
@Getter
@Builder
@EqualsAndHashCode(of = "userId")
@ToString(exclude = "password")
public final class AppUserPrincipal implements UserDetails, OAuth2User, Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

    /* =========================
       핵심 식별/인증 정보 (필수)
       ========================= */

	/**
	 * 애플리케이션 사용자 고유 식별자 = user.id (= root.id)
	 */
	private final Long userId;

	/**
	 * 로그인 아이디로 쓰이는 이메일 = user.email
	 */
	private final String email;

	/**
	 * 암호 해시(로컬 로그인에서만 사용).
	 * - 소셜 로그인 시에는 null.
	 * - DB에는 반드시 BCrypt 등 해시로 저장되어야 함.
	 */
	private final String password;

	/**
	 * 권한 목록(인가 판단의 최소 단위).
	 * - 예: ROLE_USER, ROLE_ADMIN …
	 * - SimpleGrantedAuthority("ROLE_...") 형태로 구성합니다.
	 */
	private final Collection<? extends GrantedAuthority> authorities;

    /* =========================
       계정 상태 플래그 (세션에 boolean만 보관)
       ========================= */

	/**
	 * 계정 잠김 여부(비잠김:true).
	 * - 정책 예: root.blinded_at == null → true, 값이 있으면 false
	 * - 로그인 자체를 막지 않고 기능만 제한하려면 true로 두고 비즈니스 레이어에서 제한하세요.
	 */
	private final boolean nonLocked;

	/**
	 * 계정 활성화 여부.
	 * - 정책 예: root.deleted_at == null → true, 값이 있으면 false(비활성/탈퇴)
	 */
	private final boolean enabled;

    /* =========================
       소셜 로그인 최소 식별 정보 (선택)
       ========================= */

	/**
	 * 소셜 공급자 식별자(예: "GOOGLE"). 로컬 로그인은 null.
	 */
	private final String provider;

	/**
	 * 소셜 공급자의 사용자 고유 ID(예: Google sub). 로컬 로그인은 null.
	 */
	private final String providerUserId;

    /* =========================
       팩토리 메서드
       ========================= */

	/**
	 * 로컬(폼) 로그인 사용자 → AppUserPrincipal 변환
	 *
	 * @param user 인증 전용 최소 필드만 채워진 UserDTO
	 */
	public static AppUserPrincipal fromLocal(UserDTO user) {

		return AppUserPrincipal.builder()
				.userId(user.getId())
				.email(normEmail(user.getEmail()))
				.password(user.getPassword()) // 반드시 해시(BCrypt 등)
				.authorities(toAuthorities(user.getRole())) // "ROLE_USER" 등
				.enabled(user.isEnabled())
				.nonLocked(user.isNonLocked())
				.build();
	}

	/**
	 * 소셜(OAuth2/OIDC) 로그인 사용자 → AppUserPrincipal 변환
	 *
	 * @param user 인증 전용 최소 필드만 채워진 UserDTO
	 */
	public static AppUserPrincipal fromSocial(UserDTO user, SocialAccountDTO socialAccountDTO) {

		return AppUserPrincipal.builder()
				.userId(user.getId())
				.email(normEmail(user.getEmail()))
				.password(null) // 소셜은 비번 검증 안 함 → 세션에 보관 불필요
				.authorities(toAuthorities(user.getRole())) // "ROLE_USER" 등
				.enabled(user.isEnabled())
				.nonLocked(user.isNonLocked())
				.provider(socialAccountDTO.getProvider().name())
				.providerUserId(socialAccountDTO.getProviderUserId())
				.build();
	}

    /* =========================
       OAuth2User 구현 (슬림)
       ========================= */

	private static String normEmail(String email) {
		return (email == null) ? null : email.trim().toLowerCase();
	}

	private static List<GrantedAuthority> toAuthorities(Role role) {
		// 권한(Role) 결정: null 방어 후 기본값 USER
		Role r = (role != null) ? role : Role.USER;
		return List.of(new SimpleGrantedAuthority(r.asAuthority())); // 불변
	}

	/**
	 * OAuth2User의 attributes.
	 * - 슬림 전략상 세션 팽창을 막기 위해 비워둡니다.
	 * - 클레임/프로필 정보가 필요할 땐 서비스 레이어에서 별도 조회/저장하세요.
	 */
	@Override
	public Map<String, Object> getAttributes() {
		return Map.of();
	}

	/**
	 * SecurityContext에서 "사용자 식별 문자열"로 쓰이는 값.
	 * - 여기선 내부 식별자(userId)를 문자열로 반환합니다.
	 */
	@Override
	public String getName() {
		return String.valueOf(userId);
	}

    /* =========================
       UserDetails 구현
       ========================= */

	/**
	 * username으로 이메일을 사용합니다.
	 */
	@Override
	public String getUsername() {
		return email;
	}

	/**
	 * 계정 만료 정책을 쓰지 않으므로 항상 true. 필요 시 도메인 정책에 맞게 변경하세요.
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 자격 증명(비밀번호) 만료 정책을 쓰지 않으므로 항상 true. 필요 시 변경하세요.
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

}