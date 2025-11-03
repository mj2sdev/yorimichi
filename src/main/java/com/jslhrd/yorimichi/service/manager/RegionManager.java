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
	public RegionSidoDTO findBySidoName(String sidoName) {
		return regionMapper.selectBySidoName(sidoName)
				.orElseThrow(() -> RegionNotFoundException.byName(RegionLevel.SIDO, sidoName));
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
	public RegionSigunguDTO findBySigunguName(Long sidoId, String sigunguName) {
		return regionMapper.selectBySigunguName(sidoId, sigunguName)
				.orElseThrow(() -> RegionNotFoundException.byName(RegionLevel.SIGUNGU, sigunguName));
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

	@Override
	public RegionEmdDTO findByEmdName(Long sigunguId, String emdName) {
		return regionMapper.selectByEmdName(sigunguId, emdName)
				.orElseThrow(() -> RegionNotFoundException.byName(RegionLevel.EMD, emdName));
	}
}


