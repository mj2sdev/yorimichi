package com.jslhrd.yorimichi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2ServiceAdapterConfig {

	@Bean
	public OAuth2UserService<OAuth2UserRequest, OAuth2User> socialOAuth2UserService(SocialUserManager m) {
		return m::loadOAuth2User;
	}

	@Bean
	public OAuth2UserService<OidcUserRequest, OidcUser> socialOidcUserService(SocialUserManager m) {
		return m::loadOidcUser;
	}
}
