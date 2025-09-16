package com.jslhrd.yorimichi.service;

/**
 * 소셜로그인 인터페이스 라인 구글 X 
 */
public interface SocialLoginService {
	
	/**
	 * 소셜로그인 토큰을 받으면 회원가입을 진행합니다. 
	 * @param token
	 */
	public void signup(String token);
}
