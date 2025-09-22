package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.CommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 댓글 Mapper.
 *
 * 댓글 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface CommentMapper {

	/**
	 * 댓글 추가.
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(CommentDTO dto);
	
	// TODO: 같이먹기 기준 댓글 목록 조회.

	/**
	 * 댓글 단건 조회.
	 * @param id 댓글 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<CommentDTO> selectById(@Param("id") Long id);

	/**
	 * 댓글 수정.
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(CommentDTO dto);

	/**
	 * 댓글 삭제.
	 * @param id 댓글 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("id") Long id);
}