package com.jslhrd.yorimichi.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO: 통합 검색용 DTO 정의 필요
 */
@Getter
@Setter
public class SearchDTO {

	private static final int MAX_SIZE = 100;

	/**
	 * 검색 구분: name|desc|all 등 화이트리스트
	 */
	@Pattern(regexp = "name|desc|all", message = "지원하지 않는 searchType")
	private String searchType = "all";

	/**
	 * 검색어
	 */
	@Size(max = 50)
	private String searchWord;

	/**
	 * 원하는 페이지(1-based)
	 */
	@Min(1)
	private int page = 1;

	/**
	 * 페이지당 행 수 (쿼리 limit)
	 */
	@Min(1)
	private int recordCount = 20;        // 기본 20

	/**
	 * 페이지 버튼 묶음 크기(프론트 전용)
	 */
	@Min(1)
	private int pageSize = 10;
	
	/**
	 * 총 레코드 수(서버 계산용)
	 */
	private int totalCount;

	/**
	 * 총 페이지 수(서버 계산용)
	 */
	private int totalPage;

	/**
	 * 서버에서 totalCount를 알고 난 뒤 호출
	 */
	public void computePagination() {
		// 안전한 size 상한
		if (recordCount > MAX_SIZE) recordCount = MAX_SIZE;

		// 총 페이지 계산 (0건이면 1페이지로 고정할지, 0으로 둘지는 정책 선택)
		totalPage = (totalCount == 0) ? 1
				: (int) Math.ceil((double) totalCount / recordCount);

		// page 범위 클램프(1..totalPage)
		if (page < 1) page = 1;
		if (page > totalPage) page = totalPage;
	}

	/**
	 * SQL OFFSET (1-based 페이지 기준)
	 */
	public int offset() {
		return (page - 1) * recordCount;
	}

	/**
	 * SQL LIMIT
	 */
	public int limit() {
		return recordCount;
	}

	/**
	 * 다음 페이지가 있는가?
	 */
	public boolean hasNext() {
		return page < totalPage;
	}

	/**
	 * 이전 페이지가 있는가?
	 */
	public boolean hasPrev() {
		return page > 1;
	}
}