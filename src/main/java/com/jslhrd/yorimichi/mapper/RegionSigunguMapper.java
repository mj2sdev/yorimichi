package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RegionSigunguDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RegionSigunguMapper {
    int insert(RegionSigunguDTO sigungu); // 등록
    RegionSigunguDTO selectById(Long id); // 조회
    RegionSigunguDTO selectByCode(String code); // 코드로 조회
    List<RegionSigunguDTO> listBySidoId(Long sidoId); // 특정 시/도 하위 시군구 목록
    int update(RegionSigunguDTO sigungu); // 수정
    int deleteById(Long id); // 삭제
}
