package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.domain.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StationMapper {

    /** 최신 리뷰 N건 */
    List<ReviewDTO> selectLatestReviews(@Param("count") int count);

    /** 최신 같이먹기 N건 */
    List<CoeatDTO> selectLatestCoeats(@Param("count") int count);
}
