package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BlockNotFoundException extends RuntimeException {

	public BlockNotFoundException(Long blockerId, Long blockeeId) {
		super("Block not found blocker id " + blockerId + " and blockee id " + blockeeId);
	}
}
