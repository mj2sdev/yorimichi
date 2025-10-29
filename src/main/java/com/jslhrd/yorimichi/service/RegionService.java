package com.jslhrd.yorimichi.service;


import com.jslhrd.yorimichi.domain.RegionEmdDTO;
import com.jslhrd.yorimichi.domain.RegionSidoDTO;
import com.jslhrd.yorimichi.domain.RegionSigunguDTO;

import java.util.List;

public interface RegionService {

	List<RegionSidoDTO> findAllSido();

	RegionSidoDTO findBySidoId(Long sidoId);

	List<RegionSigunguDTO> findAllSigungu();

	RegionSigunguDTO findBySigunguId(Long sigunguId);

	List<RegionEmdDTO> findAllEmd();

	RegionEmdDTO findByEmdId(Long emdId);
}
