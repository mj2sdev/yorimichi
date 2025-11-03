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

	Optional<RegionSidoDTO> selectBySidoName(@Param("sidoName") String sidoName);


	List<RegionSigunguDTO> selectAllSigungu();

	Optional<RegionSigunguDTO> selectBySigunguId(@Param("sigunguId") Long sigunguId);

	Optional<RegionSigunguDTO> selectBySigunguName(@Param("sidoId") Long sidoId,
	                                               @Param("sigunguName") String sigunguName);


	List<RegionEmdDTO> selectAllEmd();

	Optional<RegionEmdDTO> selectByEmdId(@Param("emdId") Long emdId);

	Optional<RegionEmdDTO> selectByEmdName(@Param("sigunguId") Long sigunguId,
	                                       @Param("emdName") String emdName);

	boolean existsByEmdId(@Param("emdId") Long emdId);
}
