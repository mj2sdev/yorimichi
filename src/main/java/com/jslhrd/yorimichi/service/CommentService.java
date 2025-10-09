package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.CommentDTO;

import java.util.List;

public interface CommentService {

	public List<CommentDTO> findAll();

	public List<CommentDTO> findAllByCoeatId(Long coeatId);

	public List<CommentDTO> findAllByParentId(Long coeatId, Long parentId);

	public void save(Long userId, Long coeatId, CommentDTO comment);

	public void update(Long userId, Long coeatId, Long commentId, CommentDTO comment);

	public void delete(Long userId, Long coeatId, Long commentId);
}