package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * 간단한 1-based 페이징 결과 DTO.
 * - content: 현재 페이지 데이터
 * - page: 1부터 시작
 * - size: 페이지당 개수
 * - totalCount: 전체 개수
 * - totalPages: 전체 페이지 수(최소 1)
 * - hasPrev/hasNext, prevPage/nextPage: 내비게이션용
 */
@Getter
@ToString
public final class PageResult<T> {

	private final List<T> content;
	private final int page;
	private final int size;
	private final int totalCount;
	private final int totalPages;
	private final boolean hasPrev;
	private final boolean hasNext;
	private final int prevPage;
	private final int nextPage;

	public PageResult(List<T> content, int page, int size, int totalCount) {
		if (content == null) content = Collections.emptyList();
		if (size <= 0) size = 20;
		if (page <= 0) page = 1;
		if (totalCount < 0) totalCount = 0;

		int totalPages = (totalCount == 0) ? 1 : (int) Math.ceil(totalCount / (double) size);
		if (page > totalPages) page = totalPages;

		this.content = content;
		this.page = page;
		this.size = size;
		this.totalCount = totalCount;
		this.totalPages = totalPages;
		this.hasPrev = page > 1;
		this.hasNext = page < totalPages;
		this.prevPage = hasPrev ? page - 1 : page;
		this.nextPage = hasNext ? page + 1 : page;
	}

	public static <T> PageResult<T> of(List<T> content, int page, int size, int totalCount) {
		return new PageResult<>(content, page, size, totalCount);
	}
}