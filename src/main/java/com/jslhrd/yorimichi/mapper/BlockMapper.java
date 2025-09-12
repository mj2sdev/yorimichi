package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.BlockDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BlockMapper {
    int insert(BlockDTO block); // 차단 등록
    int delete(BlockDTO block); // 차단 해제
    int count(BlockDTO block); // 차단 관계 존재 여부 (0 / 1)
    List<BlockDTO> listBlockeesOf(Long userId); // userId가 차단한 사용자들 목록
    List<BlockDTO> listBlockersOf(Long userId); // userId를 차단한 사용자들 목록
}
