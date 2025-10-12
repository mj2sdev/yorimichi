package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.KeywordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 키워드 Mapper.
 *
 * <br>키워드 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface KeywordMapper {

	List<KeywordDTO> selectAll();

	/**
	 * 키워드 단건 조회.
	 *
	 * @param keywordId 키워드 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으먄 Optional.empty()
	 */
	Optional<KeywordDTO> selectById(@Param("keywordId") Long keywordId);

	boolean existsById(@Param("keywordId") Long keywordId);

	/**
	 * 키워드 추가.
	 *
	 * @return 영향 행 수 (추가 1. 그 외 0)
	 */
	int insert(KeywordDTO keyword);

	/**
	 * 키워드 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(@Param("keywordId") Long keywordId,
	           @Param("keyword") KeywordDTO keyword);

	/**
	 * 키워드 삭제
	 *
	 * @param keywordId 키워드 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("keywordId") Long keywordId);
}