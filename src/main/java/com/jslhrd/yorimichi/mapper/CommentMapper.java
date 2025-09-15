package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.CommentDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommentMapper {
    int insert(CommentDTO comment); // 댓글 작성
    CommentDTO selectById(Long id); // 댓글 조회
    List<CommentDTO> selectByFeedId(Long feedId); // 특정 피드의 모든 댓글 조회
    List<CommentDTO> selectByParentId(Long parentId); // 특정 부모 댓글의 대댓글 목록 조회
    int updateContent(CommentDTO comment); // 댓글 내용 수정
    int deleteById(Long id); // 댓글 삭제
    int deleteByFeedId(Long feedId); // 피드 삭제 시, 해당 피드의 댓글 삭제
}
