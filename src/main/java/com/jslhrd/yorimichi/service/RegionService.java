package com.jslhrd.yorimichi.service;


import com.jslhrd.yorimichi.domain.RegionEmdDTO;
import com.jslhrd.yorimichi.domain.RegionSidoDTO;
import com.jslhrd.yorimichi.domain.RegionSigunguDTO;

import java.util.List;

public interface RegionService {

	List<RegionSidoDTO> findAllSido();

	RegionSidoDTO findBySidoId(Long sidoId);

	RegionSidoDTO findBySidoName(String sidoName);


	List<RegionSigunguDTO> findAllSigungu();

	RegionSigunguDTO findBySigunguId(Long sigunguId);

	RegionSigunguDTO findBySigunguName(Long sidoId, String sigunguName);


	List<RegionEmdDTO> findAllEmd();

	RegionEmdDTO findByEmdId(Long emdId);

	RegionEmdDTO findByEmdName(Long sigunguId, String emdName);
}