package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.exception.RootNotFoundException;
import com.jslhrd.yorimichi.exception.UserNotFoundException;
import com.jslhrd.yorimichi.mapper.LikeMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LikeManager implements LikeService {

	private final UserMapper userMapper;
	private final RootMapper rootMapper;
	private final LikeMapper likeMapper;

	@Override
	public void save(Long userId, Long rootId) {

		assertActiveUser(userId);
		assertActiveRoot(rootId);

		try {
			likeMapper.insert(userId, rootId);
		} catch (DuplicateKeyException e) {
			log.debug("Like: no-op userId={}, rootId={}", userId, rootId);
			return;
		}

		log.info("Like: created userId={}, rootId={}", userId, rootId);
	}

	@Override
	public void delete(Long userId, Long rootId) {

		boolean affected = likeMapper.delete(userId, rootId) > 0;
		if (!affected) {
			log.debug("Like: delete no-op userId={}, rootId={}", userId, rootId);
			return;
		}

		log.info("Like: deleted userId={}, rootId={}", userId, rootId);
	}

	private void assertActiveUser(Long userId) {
		boolean exist = userMapper.existsActive(userId);
		if (!exist) {
			throw new UserNotFoundException(userId);
		}
	}

	private void assertActiveRoot(Long rootId) {
		boolean exist = rootMapper.existsActive(rootId);
		if (!exist) {
			throw new RootNotFoundException(rootId);
		}
	}
}