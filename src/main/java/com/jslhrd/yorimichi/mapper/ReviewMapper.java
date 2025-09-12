package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ReviewMapper {
    int insert(ReviewDTO review); // 리뷰 작성
    ReviewDTO selectById(Long reviewId); // 리뷰 조회
    List<ReviewDTO> listByFoodId(Long foodId); // 음식별 리뷰 조회
    List<ReviewDTO> listByUserId(Long userId); // 사용자별 음식 리뷰
    int update(ReviewDTO review); // 리뷰 수정
    int deleteById(Long reviewId); // 리뷰 삭제
}
