package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.UserDTO;

/**
 * 계정관련 서비스
 * 
 * @author mj2sdev
 * 
 * @version 1.0 초안 작성
 * @version 1.1 {@code UserDetails} 삭제
 * <p>
 * 실제 필요했던건 {@code UserDetailsService}
 * 해당 인터페이스는 {@code UserService} 이쪽으로 옮김
 */
public interface AccountService {

	/**
	 * 제공된 사용자 세부 사항에 새 사용자 계정을 등록합니다.
	 *
	 * @param dto 사용자 등록 정보가 포함 된 사용자 데이터 전송 개체
	 */
	public void signup(UserDTO dto);

	/**
	 * 제공된 사용자 세부 정보에 지정된 사용자의 비밀번호를 변경합니다.
	 *
	 * @param dto 사용자 식별 및 새 비밀번호가 포함 된 사용자 데이터 전송
	 *            객체
	 */
	public void changePassword(UserDTO dto);

	/**
	 * 제공된 사용자 ID에 해당하는 사용자 계정을 삭제합니다.
	 *
	 * @param userId 계정을 삭제 해야하는 사용자의 고유 식별자
	 */
	public void delete(Long userId);

	/**
	 * 제공된 토큰으로 소셜 인증을 사용하여 새 사용자 계정을
	 * 등록합니다.
	 *
	 * @param token 토큰 사용자 등록에 사용되는 소셜 인증 토큰
	 */
	public void signupSocial(String token);

	/**
	 * 사용자가 입력한 닉네임이 DB에서 중복되어 있는지 유효성검사
	 *
	 * @param nickname
	 */
	public boolean validateNickname(String nickname);

	/**
	 * 이메일 인증을 구현해야 합니다.
	 * @param email
	 * @return
	 */
	public boolean verificateEmail(String email);
}
