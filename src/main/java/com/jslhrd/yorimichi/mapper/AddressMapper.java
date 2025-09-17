package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.AddressDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {

    int insert(AddressDTO address);                          // 주소 등록
    AddressDTO selectById(@Param("id") Long id);            // 주소 조회(PK)
    int update(AddressDTO address);                          // 주소 수정
    int deleteById(@Param("id") Long id);                   // 주소 삭제(PK)
    List<AddressDTO> listByRoadId(@Param("roadId") Long roadId); // 도로에 속한 주소목록
}
