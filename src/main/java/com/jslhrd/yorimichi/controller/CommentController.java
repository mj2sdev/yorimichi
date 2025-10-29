package com.jslhrd.yorimichi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jslhrd.yorimichi.domain.CommentDTO;
import com.jslhrd.yorimichi.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("comment")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	void writeComment(@RequestBody CommentDTO comment, @AuthenticationPrincipal(expression = "userId") Long userId) {
		log.debug("[{}] 유저가 댓글을 작성합니다. 내용:[{}]", userId, comment.getContent());
		commentService.save(userId, comment.getCoeatId(), comment);
	}

}
