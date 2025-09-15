package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RegionSidoDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RegionSidoMapper {
    int insert(RegionSidoDTO sido); // 등록
    RegionSidoDTO selectById(Long id); // 조회
    RegionSidoDTO selectByCode(String code); // 코드로 조회
    List<RegionSidoDTO> listAll(); // 전체 목록
    int update(RegionSidoDTO sido); // 수정
    int deleteById(Long id); // 삭제
}
