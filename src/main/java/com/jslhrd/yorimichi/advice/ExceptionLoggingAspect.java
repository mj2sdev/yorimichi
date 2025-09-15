package com.jslhrd.yorimichi.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {
	
	@AfterThrowing(pointcut = "execution(* com.jslhrd.yorimichi..*(..))", throwing = "ex")
	public void logException(JoinPoint joinPoint, Exception ex) {
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		log.error("‼️ 예외 발생 - {}.{}() : {}", className, methodName, ex.getMessage());
	}
}
