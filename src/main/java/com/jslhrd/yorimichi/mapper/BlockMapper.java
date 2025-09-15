package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.BlockDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlockMapper {

    int insert(BlockDTO block);  // 차단 등록

    // 차단 해제(복합키) — DTO 혼용 금지, 키를 명시적으로 전달
    int delete(@Param("blockerId") Long blockerId,
               @Param("blockeeId") Long blockeeId);

    // 차단 관계 존재 여부 (0/1)
    int count(@Param("blockerId") Long blockerId,
              @Param("blockeeId") Long blockeeId);

    // 내가 차단한 사용자 목록(= blockee 리스트)
    List<BlockDTO> listBlockeesOf(@Param("blockerId") Long blockerId);

    // 나를 차단한 사용자 목록(= blocker 리스트)
    List<BlockDTO> listBlockersOf(@Param("blockeeId") Long blockeeId);
}
