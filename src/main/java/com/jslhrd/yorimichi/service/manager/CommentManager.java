package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.CommentDTO;
import com.jslhrd.yorimichi.exception.*;
import com.jslhrd.yorimichi.mapper.*;
import com.jslhrd.yorimichi.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentManager implements CommentService {

	private final RootMapper rootMapper;
	private final UserMapper userMapper;
	private final CoeatMapper coeatMapper;
	private final CommentMapper commentMapper;
	private final CoeatRequestMapper coeatRequestMapper;

	@Override
	public List<CommentDTO> findAll() {
		// TODO: 무한 스크룰 및 comment 상세 정보 추후 구현
		return commentMapper.selectAll();
	}

	@Override
	public List<CommentDTO> findAllByCoeatId(Long coeatId) {
		// TODO: 무한 스크룰 및 comment 상세 정보 추후 구현
		return commentMapper.selectAllByCoeatId(coeatId);
	}

	@Override
	public List<CommentDTO> findAllByParentId(Long coeatId, Long parentId) {
		return commentMapper.selectAllByParentId(coeatId, parentId);
	}

	@Override
	@Transactional
	public void save(Long userId, Long coeatId, CommentDTO comment) {

		assertActiveUser(userId);
		assertActiveCoeat(coeatId);
		assertActiveParent(coeatId, comment.getParentId());
		assertCanComment(userId, coeatId);

		comment.setUserId(userId);
		comment.setCoeatId(coeatId);

		rootMapper.insert(comment);
		if (comment.getId() == null) {
			throw new IllegalStateException("Root: insert failed or no generated commentId");
		}

		commentMapper.insert(comment);
		log.info("Comment: created commentId={}", comment.getId());
	}

	@Override
	@Transactional
	public void update(Long userId, Long coeatId, Long commentId, CommentDTO comment) {

		if (comment.getId() != null && !commentId.equals(comment.getId())) {
			throw new BadRequestException("경로의 commentId 와 본문의 id 가 다릅니다.");
		}

		boolean affected = commentMapper.update(userId, coeatId, commentId, comment) > 0;
		if (!affected) {
			assertActiveComment(coeatId, commentId);
			throw new ForbiddenException("댓글 수정 권한이 없습니다.");
		}

		log.info("Comment: updated commentId={}", commentId);
	}

	@Override
	@Transactional
	public void delete(Long userId, Long coeatId, Long commentId) {

		boolean affected = commentMapper.deleteById(userId, coeatId, commentId) > 0;
		if (!affected) {
			assertActiveComment(coeatId, commentId);
			throw new ForbiddenException("댓글 삭제 권한이 없습니다.");
		}

		log.info("Comment: soft deleted commentId={}", commentId);
	}

	private void assertActiveUser(Long userId) {
		boolean exists = userMapper.existsActive(userId);
		if (!exists) {
			throw new UserNotFoundException(userId);
		}
	}

	private void assertActiveCoeat(Long coeatId) {
		boolean exists = coeatMapper.existsActive(coeatId);
		if (!exists) {
			throw new CoeatNotFoundException(coeatId);
		}
	}

	private void assertActiveParent(Long coeatId, Long parentId) {
		if (parentId == null) {
			return;
		}
		boolean exists = commentMapper.existsActive(coeatId, parentId);
		if (!exists) {
			throw new CommentNotFoundException(coeatId, parentId);
		}
	}

	private void assertCanComment(Long userId, Long coeatId) {
		boolean isOwner = coeatMapper.isOwner(userId, coeatId);
		if (isOwner) {
			return;
		}
		boolean approved = coeatRequestMapper.isApproved(userId, coeatId);
		if (!approved) {
			throw new ForbiddenException("승인된 참가자만 댓글을 작성할 수 있습니다.");
		}
	}

	private void assertActiveComment(Long coeatId, Long commentId) {
		boolean exists = commentMapper.existsActive(coeatId, commentId);
		if (!exists) {
			throw new CommentNotFoundException(coeatId, commentId);
		}
	}
}