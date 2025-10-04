package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.exception.*;
import com.jslhrd.yorimichi.mapper.BlockMapper;
import com.jslhrd.yorimichi.mapper.FollowMapper;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RelationshipManager implements RelationshipService {

	private final UserMapper userMapper;
	private final FollowMapper followMapper;
	private final BlockMapper blockMapper;

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findFollowees(Long userId) {
		return followMapper.selectFolloweesByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findFollowers(Long userId) {
		return followMapper.selectFollowersByUserId(userId);
	}

	@Override
	public void saveFollow(Long followerId, Long followeeId) {

		if (followerId.equals(followeeId)) {
			throw new IllegalArgumentException("자기 자신은 팔로우할 수 없습니다.");
		}

		boolean exists = userMapper.existsActive(followeeId);
		if (!exists) {
			throw new UserNotFoundException(followeeId);
		}

		if (blockMapper.exists(followerId, followeeId) || blockMapper.exists(followeeId, followerId)) {
			throw new AccessDeniedException("차단 상태에서는 팔로우할 수 없습니다.");
		}

		try {
			int affected = followMapper.insert(followerId, followeeId);
			if (affected == 0) {
				log.warn("Follow insert failed: affected={}, followerId={}, followeeId={}", affected, followerId, followeeId);
				throw new IllegalStateException("Follow insert failed");
			}
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateFollowException(followerId, followeeId);
		}

		log.info("Follow created followerId={}, followeeId={}", followerId, followeeId);
	}

	@Override
	public void updateFollowNotification(Long followerId, Long followeeId, Boolean notified) {

		int affected = followMapper.updateNotification(followerId, followeeId, notified);
		if (affected == 1) {
			log.info("Follow notification updated followerId={}, followeeId={}, notified={}", followerId, followeeId, notified);
			return;
		}

		throw new FollowNotFoundException(followerId, followeeId);
	}


	@Override
	public void deleteFollow(Long followerId, Long followeeId) {

		int affected = followMapper.delete(followerId, followeeId);
		if (affected == 1) {
			log.info("Follow deleted followerId={}, followeeId={}", followerId, followeeId);
			return;
		}

		throw new FollowNotFoundException(followerId, followeeId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findBlocks(Long userId) {
		return blockMapper.selectAllByUserId(userId);
	}

	@Override
	public void saveBlock(Long blockerId, Long blockeeId) {

		if (blockerId.equals(blockeeId)) {
			throw new IllegalArgumentException("자기 자신은 차단할 수 없습니다.");
		}

		boolean exists = userMapper.existsActive(blockeeId);
		if (!exists) {
			throw new UserNotFoundException(blockeeId);
		}

		try {
			int affected = blockMapper.insert(blockerId, blockeeId);
			if (affected == 0) {
				log.warn("Block insert failed: affected={}, blockerId={}, blockeeId={}", affected, blockerId, blockeeId);
				throw new IllegalStateException("Block insert failed");
			}
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateBlockException(blockerId, blockeeId);
		}

		log.info("Block created blockerId={}, blockeeId={}", blockerId, blockeeId);

		int affected = followMapper.deleteBothDirections(blockerId, blockeeId);
		log.info("Unfollow both directions done: {}↔{}, affected={}", blockerId, blockeeId, affected);

		// TODO: (선택) 요청 취소 등
		// coeatRequestMapper.cancelAllBetween(blockerId, blockeeId);
	}

	@Override
	public void deleteBlock(Long blockerId, Long blockeeId) {

		int affected = blockMapper.delete(blockerId, blockeeId);
		if (affected == 1) {
			log.info("Block deleted blockerId={}, blockeeId={}", blockerId, blockeeId);
			return;
		}

		throw new BlockNotFoundException(blockerId, blockeeId);
	}
}