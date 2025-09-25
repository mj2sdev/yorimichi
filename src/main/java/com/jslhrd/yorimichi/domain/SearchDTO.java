package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO: 통합 검색용 DTO 정의 필요
 */
@Getter
@Setter
public class SearchDTO {

	private String searchType;
	private String searchWord;

	/**
	 * 원하는 페이지
	 */
	private int page;
	/**
	 * 페이지당 글 갯수
	 */
	private int recordCount;
	/**
	 * 페이지 사이즈
	 */
	private int pageSize;
	/**
	 * 총 몇 페이지
	 */
	private int totalPage;
	/**
	 * 검색물 갯수
	 */
	private int totalCount;

	public void computePagination() {
		// 총 페이지 수 계산
		if (recordCount > 0) {
			totalPage = (totalCount + recordCount - 1) / recordCount;
		} else {
			totalPage = 0; // 또는 예외 처리
		}

		// 현재 페이지가 총 페이지 수를 초과하지 않도록 조정
		if (page > totalPage) {
			page = totalPage;
		}
		if (page < 1 && totalPage > 0) { // 페이지가 1보다 작고 총 페이지가 있을 경우 1로 설정
			page = 1;
		} else if (totalPage == 0) { // 총 페이지가 0일 경우 페이지도 0으로 설정
			page = 0;
		}

	}
}
