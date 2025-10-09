package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class CommentNotFoundException extends DomainException {

	public CommentNotFoundException(Long coeatId, Long commentId) {
		super(
				"COMMENT_NOT_FOUND",
				"댓글를 찾을 수 없습니다.",
				Map.of(
						"coeatId", coeatId,
						"commentId", commentId
				)
		);
	}
}