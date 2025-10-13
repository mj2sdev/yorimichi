package com.jslhrd.yorimichi.security;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
class SocialOAuth2UserServiceAdapter implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final SocialUserManager manager;

	public SocialOAuth2UserServiceAdapter(SocialUserManager manager) {
		this.manager = manager;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest req) {
		return manager.loadOAuth2User(req);
	}
}