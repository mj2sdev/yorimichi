package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.CommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 댓글 Mapper.
 * <p>
 * 댓글 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface CommentMapper {

	List<CommentDTO> selectAll();

	List<CommentDTO> selectAllByCoeatId(@Param("coeatId") Long coeatId);

	List<CommentDTO> selectAllByParentId(@Param("coeatId") Long coeatId,
	                                     @Param("parentId") Long parentId);

	boolean existsActive(@Param("coeatId") Long coeatId,
	                     @Param("commentId") Long commentId);

	/**
	 * 댓글 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(CommentDTO comment);

	/**
	 * 댓글 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(@Param("userId") Long userId,
	           @Param("coeatId") Long coeatId,
	           @Param("commentId") Long commentId,
	           @Param("comment") CommentDTO comment);

	/**
	 * 댓글 삭제.
	 *
	 * @param commentId 댓글 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("userId") Long userId,
	               @Param("coeatId") Long coeatId,
	               @Param("commentId") Long commentId);
}