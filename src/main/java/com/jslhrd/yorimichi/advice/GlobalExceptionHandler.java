package com.jslhrd.yorimichi.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public String handleNotFound(NoHandlerFoundException e, Model model){
		model.addAttribute("path", e.getRequestURL());
		model.addAttribute("message", "요청하신 페이지를 찾을 수 없습니다.");
		return "error/notFound";
	}
}
