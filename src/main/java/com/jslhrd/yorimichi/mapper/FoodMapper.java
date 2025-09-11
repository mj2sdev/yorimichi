package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FoodDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface FoodMapper {
    int insert(FoodDTO food); // 메뉴 등록
    FoodDTO selectById(Long foodId); // 메뉴 조회
    List<FoodDTO> listByStoreId(Long storeId); // 메장별 메뉴 목록
    int update(FoodDTO food); // 메뉴 수정
    int deleteById(Long foodId); // 메뉴 삭제
}
