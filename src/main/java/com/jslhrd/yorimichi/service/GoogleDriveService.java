package com.jslhrd.yorimichi.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * 구글드라이브 API 를 이용해 파일관리를 구현합니다.
 */
public interface GoogleDriveService {

	/**
	 * String MultipartFile 을 받아 구글드라이브에 저장하고 이미지 링크를 생성해 반환합니다.
	 * @param file
	 * @return String 이미지 링크를 반환합니다.
	 */
	public String uploadFile(MultipartFile file);
}
