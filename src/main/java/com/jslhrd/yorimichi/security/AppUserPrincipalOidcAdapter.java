package com.jslhrd.yorimichi.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;

public class AppUserPrincipalOidcAdapter extends DefaultOidcUser {

	private final AppUserPrincipal app;

	public AppUserPrincipalOidcAdapter(AppUserPrincipal app,
	                                   Collection<? extends GrantedAuthority> authorities,
	                                   OidcIdToken idToken,
	                                   OidcUserInfo userInfo
	) {
		super(authorities, idToken, userInfo, "sub");
		this.app = app;
	}

	public AppUserPrincipal geeApp() {
		return app;
	}
}