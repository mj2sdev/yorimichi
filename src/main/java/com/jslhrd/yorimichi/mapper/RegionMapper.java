package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RegionEmdDTO;
import com.jslhrd.yorimichi.domain.RegionSidoDTO;
import com.jslhrd.yorimichi.domain.RegionSigunguDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RegionMapper {

	List<RegionSidoDTO> selectAllSido();

	Optional<RegionSidoDTO> selectBySidoId(@Param("sidoId") Long sidoId);

	List<RegionSigunguDTO> selectAllSigungu();

	Optional<RegionSigunguDTO> selectBySigunguId(@Param("sigunguId") Long sigunguId);

	List<RegionEmdDTO> selectAllEmd();

	Optional<RegionEmdDTO> selectByEmdId(@Param("emdId") Long emdId);
}
