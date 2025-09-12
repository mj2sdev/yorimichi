package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RegionEmdDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RegionEndMapper {
    int insert(RegionEmdDTO emd); // 등록
    RegionEmdDTO selectById(Long id); // 조회
    RegionEmdDTO selectByCode(String code); // 코드로 조회
    List<RegionEmdDTO> listBySigunguId(Long sigunguId); // 특정 시군구 하위 읍면동 조회
    int update(RegionEmdDTO emd); // 수정
    int deleteById(Long id); // 삭제
}
