package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.exception.RootNotFoundException;
import com.jslhrd.yorimichi.mapper.LikeMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LikeManager implements LikeService {

	private final RootMapper rootMapper;
	private final LikeMapper likeMapper;

	@Override
	public void save(Long userId, Long rootId) {

		boolean exists = rootMapper.existsActive(rootId);
		if (!exists) {
			throw new RootNotFoundException(rootId);
		}

		try {
			likeMapper.insert(userId, rootId);
		} catch (DataIntegrityViolationException e) {
			log.debug("Like already exists: userId={}, rootId={}", userId, rootId);
			return;
		}

		log.info("Like created userId={}, rootId={}", userId, rootId);
	}

	@Override
	public void delete(Long userId, Long rootId) {

		boolean affected = likeMapper.delete(userId, rootId) > 0;
		if (affected) {
			log.info("Like deleted userId={}, rootId={}", userId, rootId);
		}
	}
}