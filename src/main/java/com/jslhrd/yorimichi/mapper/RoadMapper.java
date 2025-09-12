package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RoadDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RoadMapper {
    int insert(RoadDTO road); // 등록
    RoadDTO selectById(Long id); // 조회
    RoadDTO selectByCode(String code); // 코드로 조회
    List<RoadDTO> listByEmdId(Long emdId); // 특정 읍면동 하위 도로 목록
    int update(RoadDTO road); // 수정
    int deleteById(Long id); // 삭제
}
