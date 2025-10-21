package com.jslhrd.yorimichi;

import java.io.Console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @author mj2sdev
 * @version 1.1
 * db password 입력 관련 부분 추가
 */
@SpringBootApplication
public class YorimichiApplication {

	private static final String DB_PASSWORD = "DB_PASSWORD";

	public static void main(String[] args) {
		initializeDatabasePassword();
		SpringApplication.run(YorimichiApplication.class, args);
	}

	/**
	 * <h1>데이터베이스 비밀번호 초기화 함수</h1>
	 * yml에서 개인 브랜치마다 바꾸고 해당 파일 커밋 조심하고 하던 시대는 끝났습니다.
	 * <p>이제 서버 킬 때 입력하세요!
	 * <p>또는 환경변수 DB_PASSWORD에 미리 정의하세요!
	 * <p>해당 함수로 당신의 개발환경의 질을 향상시키세요!
	 */
	private static void initializeDatabasePassword() {
		String pwd = System.getenv(DB_PASSWORD);
		
		if (pwd == null || pwd.isEmpty()) {
			pwd = System.getProperty(DB_PASSWORD);
		}

		if (pwd == null || pwd.isEmpty()) {
			Console console = System.console();
			if (console != null) {
				char[] passChars = console.readPassword("Enter DB password: ");

				if (passChars != null) {
					pwd = new String(passChars);
					System.setProperty(DB_PASSWORD, pwd);
				}
			} else {
				try {
					System.out.println("Enter DB password (stdin): ");
					byte[] input = new byte[256];
					int r = System.in.read(input);
					if (r > 0) {
						pwd = new String(input, 0, r).trim();
						System.setProperty(DB_PASSWORD, pwd);
					}
				} catch (Exception e) {
					
				}
			}
		}
	}
}
