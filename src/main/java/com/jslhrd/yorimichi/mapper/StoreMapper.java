package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.StoreDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface StoreMapper {
    int insert(StoreDTO store); // 상점 등록
    StoreDTO selectById(Long storeId); // 상점 정보 조회
    int update(StoreDTO store); // 상점 정보 수정
    int deleteById(Long storeId); // 상점 삭제
    List<StoreDTO> listByAddressId(Long addressId); // 주소로 상점 목록 조회
    List<StoreDTO> listByNameLike(String keyword); // 키워드로 상점 목록 조회
}
