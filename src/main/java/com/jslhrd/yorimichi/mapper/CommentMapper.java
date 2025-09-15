package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.CommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

	/* 댓글 작성 */
	int insert(CommentDTO comment);

	/* 단건 조회 */
	CommentDTO selectById(@Param("id") Long id);

	/* 특정 피드의 모든 댓글 조회 */
	List<CommentDTO> selectByFeedId(@Param("feedId") Long feedId);

	/* 특정 부모 댓글의 대댓글 목록 조회 */
	List<CommentDTO> selectByParentId(@Param("parentId") Long parentId);

	/* 댓글 내용 수정 */
	int updateContent(CommentDTO comment);

	/* 댓글 삭제 (PK) */
	int deleteById(@Param("id") Long id);

	/* 피드 삭제 시, 해당 피드의 댓글 일괄 삭제 */
	int deleteByFeedId(@Param("feedId") Long feedId);
}
