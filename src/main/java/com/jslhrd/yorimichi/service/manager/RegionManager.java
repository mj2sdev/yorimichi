package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.RegionEmdDTO;
import com.jslhrd.yorimichi.domain.RegionSidoDTO;
import com.jslhrd.yorimichi.domain.RegionSigunguDTO;
import com.jslhrd.yorimichi.enums.RegionLevel;
import com.jslhrd.yorimichi.exception.RegionNotFoundException;
import com.jslhrd.yorimichi.mapper.RegionMapper;
import com.jslhrd.yorimichi.service.RegionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionManager implements RegionService {

	private final RegionMapper regionMapper;

	@Override
	public List<RegionSidoDTO> findAllSido() {
		return regionMapper.selectAllSido();
	}

	@Override
	public RegionSidoDTO findBySidoId(Long sidoId) {
		return regionMapper.selectBySidoId(sidoId)
				.orElseThrow(() -> RegionNotFoundException.byId(RegionLevel.SIDO, sidoId));
	}

	@Override
	public List<RegionSigunguDTO> findAllSigungu() {
		return regionMapper.selectAllSigungu();
	}

	@Override
	public RegionSigunguDTO findBySigunguId(Long sigunguId) {
		return regionMapper.selectBySigunguId(sigunguId)
				.orElseThrow(() -> RegionNotFoundException.byId(RegionLevel.SIGUNGU, sigunguId));
	}

	@Override
	public List<RegionEmdDTO> findAllEmd() {
		return regionMapper.selectAllEmd();
	}

	@Override
	public RegionEmdDTO findByEmdId(Long emdId) {
		return regionMapper.selectByEmdId(emdId)
				.orElseThrow(() -> RegionNotFoundException.byId(RegionLevel.EMD, emdId));
	}
}


