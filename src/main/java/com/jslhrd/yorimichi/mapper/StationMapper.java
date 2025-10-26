package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.response.StationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StationMapper {

	List<StationDTO> selectSlice(
			@Param("nextId") Long nextId,
			@Param("size") int size
	);
}