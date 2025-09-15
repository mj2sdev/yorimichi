package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.AddressDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AddressMapper {
    int insert(AddressDTO address); // 주소 등록
    AddressDTO selectById(Long addressId); // 주소 조회
    int update(AddressDTO address); // 주소 수정
    int deleteById(Long addressId); // 주소 삭제
    List<AddressDTO> listByRoadId(Long roadId); // 특정 도로에 속한 주소 목록
}
