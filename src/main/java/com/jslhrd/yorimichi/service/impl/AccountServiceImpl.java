package com.jslhrd.yorimichi.service.impl;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.mapper.RoleMapper;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.service.AccountService;
import com.jslhrd.yorimichi.service.social.SocialAuthClient;
import com.jslhrd.yorimichi.service.social.SocialProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 계정 서비스 구현체.
 *
 * <p>주요 책임:
 * <ul>
 *   <li>회원 가입</li>
 *   <li>비밀번호 변경</li>
 *   <li>회원 삭제</li>
 *   <li>소셜 가입</li>
 * </ul>
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final long DEFAULT_ROLE_ID = 1L; // TODO: 설정/조회로 대체

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final SocialAuthClient socialAuthClient;

    /**
     * 회원 가입.
     * <p>이메일 중복 검사 → 역할 유효성 검사 → 비밀번호 인코딩 → INSERT</p>
     *
     * @param dto 사용자 등록 정보
     */
    @Override
    public void signup(final UserDTO dto) {
        if (userMapper.selectByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + dto.getEmail());
        }

        roleMapper.selectById(dto.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 역할 ID: " + dto.getRoleId()));

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        int inserted = userMapper.insert(dto);
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
        if (userId == null) throw new IllegalArgumentException("삭제할 사용자 ID가 필요합니다.");
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
}
