package com.jslhrd.yorimichi.service.impl;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.service.AccountService;
import com.jslhrd.yorimichi.service.social.SocialAuthClient;
import com.jslhrd.yorimichi.service.social.SocialProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * AccountService 기본 플로우 통합 테스트.
 *
 * <p>왜 이 테스트를 하나?
 * <ul>
 *   <li>이메일 중복 방지 로직 검증</li>
 *   <li>AUTO_INCREMENT 전략에서 id 생성/매핑 검증</li>
 *   <li>비밀번호 인코딩(BCrypt) 검증</li>
 *   <li>소셜 토큰 기반 가입 플로우 검증(테스트 더블 사용)</li>
 * </ul>
 * </p>
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AccountServiceImplTest {

    private static final Long ROLE_USER_ID = 1L; // data.sql에서 삽입된 USER 롤

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 회원가입 성공: AUTO_INCREMENT id 생성 + 비번 인코딩 확인.
     */
    @Test
    @DisplayName("회원가입 성공 - AUTO_INCREMENT id 생성 & 비번 인코딩")
    @Rollback
    void signup_success_auto_increment() {
        // given
        UserDTO dto = new UserDTO();
        dto.setRoleId(ROLE_USER_ID);
        dto.setEmail("signup@test.com");
        dto.setPassword("plain-password");
        dto.setNickname("tester");
        dto.setDescription("desc");

        // when
        accountService.signup(dto);

        // then
        Optional<UserDTO> found = userMapper.selectByEmail("signup@test.com");
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isNotNull().isPositive();
        assertThat(found.get().getRoleId()).isEqualTo(ROLE_USER_ID);
        // 비밀번호는 인코딩되어 저장되어야 함
        assertThat(found.get().getPassword()).isNotEqualTo("plain-password");
        assertThat(passwordEncoder.matches("plain-password", found.get().getPassword())).isTrue();
    }

    /**
     * 회원가입 실패: 이메일 중복.
     */
    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    @Rollback
    void signup_fail_duplicate_email() {
        // given
        UserDTO first = new UserDTO();
        first.setRoleId(ROLE_USER_ID);
        first.setEmail("dup@test.com");
        first.setPassword("x");
        first.setNickname("dup1");
        accountService.signup(first);

        UserDTO dup = new UserDTO();
        dup.setRoleId(ROLE_USER_ID);
        dup.setEmail("dup@test.com"); // 같은 이메일
        dup.setPassword("y");
        dup.setNickname("dup2");

        // when & then
        assertThrows(IllegalArgumentException.class, () -> accountService.signup(dup));
    }

    /**
     * 비밀번호 변경 성공: 인코딩 후 저장 검증.
     */
    @Test
    @DisplayName("비밀번호 변경 성공 - 인코딩 검증")
    @Rollback
    void changePassword_success() {
        // given: 유저 선등록
        UserDTO dto = new UserDTO();
        dto.setRoleId(ROLE_USER_ID);
        dto.setEmail("pwd@test.com");
        dto.setPassword("before");
        dto.setNickname("np");
        accountService.signup(dto);

        Long id = userMapper.selectByEmail("pwd@test.com").orElseThrow().getId();

        // when: 비밀번호 변경
        UserDTO change = new UserDTO();
        change.setId(id);
        change.setPassword("after");
        accountService.changePassword(change);

        // then
        UserDTO after = userMapper.selectById(id).orElseThrow();
        assertThat(after.getPassword()).isNotEqualTo("after"); // 인코딩되어야 함
        assertThat(passwordEncoder.matches("after", after.getPassword())).isTrue();
        assertThat(passwordEncoder.matches("before", after.getPassword())).isFalse();
    }

    /**
     * 소셜 가입 성공: 테스트 더블 SocialAuthClient로 검증.
     */
    @Test
    @DisplayName("소셜 가입 성공 - SocialAuthClient 더블 사용")
    @Rollback
    void signupSocial_success() {
        // when
        accountService.signupSocial("DUMMY_TOKEN");

        // then
        Optional<UserDTO> found = userMapper.selectByEmail("social@test.com");
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isNotNull().isPositive();
        assertThat(found.get().getNickname()).isEqualTo("social-user");
        assertThat(found.get().getRoleId()).isEqualTo(ROLE_USER_ID);
        assertThat(found.get().getPassword()).isNotBlank(); // 랜덤 생성 + 인코딩 저장
    }

    /**
     * 소셜 가입 실패: 이미 해당 이메일로 가입된 경우.
     */
    @Test
    @DisplayName("소셜 가입 실패 - 기존 이메일 존재")
    @Rollback
    void signupSocial_fail_duplicate() {
        // given: 동일 이메일로 사전 가입
        UserDTO dto = new UserDTO();
        dto.setRoleId(ROLE_USER_ID);
        dto.setEmail("social@test.com");
        dto.setPassword("plain");
        dto.setNickname("already");
        accountService.signup(dto);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> accountService.signupSocial("DUMMY_TOKEN"));
    }

    // ----------------------------------------------------------------------
    // 테스트 전용 설정: PasswordEncoder / SocialAuthClient 더블
    // ----------------------------------------------------------------------
    @TestConfiguration
    static class TestBeans {
        /**
         * 테스트에서도 실제와 동일한 BCrypt 사용.
         */
        @Bean
        @Primary
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        /**
         * 소셜 인증 더블:
         * 토큰 → 고정된 프로필 반환(구현체 없이 서비스 레이어 검증).
         */
        @Bean
        @Primary
        SocialAuthClient socialAuthClient() {
            return token -> new SocialProfile("social@test.com", "social-user");
        }
    }
}
