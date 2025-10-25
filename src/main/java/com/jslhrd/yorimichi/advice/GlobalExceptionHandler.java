package com.jslhrd.yorimichi.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public String handleNotFound(NoHandlerFoundException e, Model model) {
		model.addAttribute("path", e.getRequestURL());
		model.addAttribute("message", "요청하신 페이지를 찾을 수 없습니다.");
		return "error/general";
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public String handleNoResource(NoResourceFoundException e, Model model) {
		model.addAttribute("message", "요청하신 페이지를 찾을 수 없습니다.");
		return "error/general";
	}

	@ExceptionHandler(Exception.class)
	public String handlerGeneral(Exception e, Model model) {
		model.addAttribute("message", "예기치 못한 오류가 발생했습니다.");
		return "error/general";
	}
}
