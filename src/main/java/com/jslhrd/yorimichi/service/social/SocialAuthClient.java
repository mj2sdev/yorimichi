package com.jslhrd.yorimichi.service.social;

/**
 * 소셜 인증 토큰 검증 클라이언트.
 *
 * <p>왜 분리하나?<br/>
 * - 공급자 의존성을 서비스에서 분리(테스트/모킹 쉬움).
 * </p>
 */
public interface SocialAuthClient {
    SocialProfile verify(String token);
}
