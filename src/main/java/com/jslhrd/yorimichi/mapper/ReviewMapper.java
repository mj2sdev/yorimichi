package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ReviewMapper {
	int insert(ReviewDTO dto); // 리뷰 등록
	ReviewDTO selectById(@Param("id") Long id); // 단건 조회
	List<ReviewDTO> selectByStoreId(@Param("storeId") Long storeId); // 매장 기준 목록
	List<ReviewDTO> selectByUserId(@Param("userId") Long userId); // 사용자 기준 목록
	int update(ReviewDTO dto); // 내용/평점 수정
	int deleteById(@Param("id") Long id); // 삭제(PK)
	int deleteByStoreId(@Param("storeId") Long storeId); // 매장 삭제 시 일괄 삭제
	int deleteByUserId(@Param("userId") Long userId); // 사용자 탈퇴 시 일괄 삭제
}
