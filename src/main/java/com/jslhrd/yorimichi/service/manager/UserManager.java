package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManager implements UserService {

	@Override
	public void delete(Long userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<UserDTO> findAll(SearchDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO findById(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(UserDTO dto) {
		// TODO Auto-generated method stub

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
