package com.jslhrd.yorimichi.security;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
class SocialOidcUserServiceAdapter implements OAuth2UserService<OidcUserRequest, OidcUser> {

	private final SocialUserManager manager;

	public SocialOidcUserServiceAdapter(SocialUserManager manager) {
		this.manager = manager;
	}

	@Override
	public OidcUser loadUser(OidcUserRequest req) {
		return manager.loadOidcUser(req);
	}
}