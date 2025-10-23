package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.exception.DuplicateNicknameException;
import com.jslhrd.yorimichi.exception.UserNotFoundException;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserManager implements UserService {

	private final UserMapper userMapper;
	private final AccountManager accountManager;

	@Override
	public List<UserDTO> findAll(SearchDTO search) {
		return userMapper.selectAll(search);
	}

	@Override
	public UserDTO findById(Long userId) {
		return userMapper.selectById(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));
	}

	@Override
	public boolean isNicknameAvailable(String nickname) {
		return !userMapper.existsByNickname(nickname.trim());
	}

	@Override
	@Transactional
	public boolean update(Long userId, UserDTO user) {

		String trimmedNickname = user.getNickname().trim();

		if (trimmedNickname.isEmpty()) {
			throw new IllegalArgumentException("닉네임이 올바르지 않습니다.");
		}

		user.setNickname(trimmedNickname);

		try {
			boolean affected = userMapper.update(userId, user) > 0;
			if (!affected) {
				assertActiveUser(userId);
				log.debug("User: update no-op userId={}", userId);
				return false;
			}
		} catch (DuplicateKeyException e) {
			throw new DuplicateNicknameException(trimmedNickname);
		}

		log.info("User: update userId={}", userId);
		return true;
	}

	private void assertActiveUser(Long userId) {
		boolean affected = userMapper.existsActive(userId);
		if (!affected) {
			throw new UserNotFoundException(userId);
		}
	}
}