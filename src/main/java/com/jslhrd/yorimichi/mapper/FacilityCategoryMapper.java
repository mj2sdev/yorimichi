package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FacilityCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface FacilityCategoryMapper {
    int insert(FacilityCategoryDTO facilityCategory); // 시설 카테고리 등록
    FacilityCategoryDTO selectById(Long facilityCategoryId); // 조회
    List<FacilityCategoryDTO> listAll(); // 전체 목록
    int update(FacilityCategoryDTO facilityCategory); // 수정
    int deleteById(Long facilityCategoryId); // 삭제
}
