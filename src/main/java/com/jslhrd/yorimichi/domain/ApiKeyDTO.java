package com.jslhrd.yorimichi.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * @author mj2sdev
 * @version 1.0
 * 초안 작성
 * <p>
 * API KEY 데이터를 Database 에 저장하고 불러오기 위해 사용하는 DTO 입니다.
 */
@Getter
@Builder
public class ApiKeyDTO {
	
	private String name;
	private String description;
	private String value;
	private String owner;
}
