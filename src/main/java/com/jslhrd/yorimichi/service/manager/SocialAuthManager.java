package com.jslhrd.yorimichi.service.manager;

import org.springframework.stereotype.Service;

import com.jslhrd.yorimichi.service.social.SocialAuthClient;
import com.jslhrd.yorimichi.service.social.SocialProfile;

@Service
public class SocialAuthManager implements SocialAuthClient{

	@Override
	public SocialProfile verify(String token) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'verify'");
	}
	
}
