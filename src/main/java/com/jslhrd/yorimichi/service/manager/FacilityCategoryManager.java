package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.FacilityCategoryDTO;
import com.jslhrd.yorimichi.exception.BadRequestException;
import com.jslhrd.yorimichi.exception.DuplicateFacilityCategoryException;
import com.jslhrd.yorimichi.exception.FacilityCategoryNotFoundException;
import com.jslhrd.yorimichi.exception.StoreNotFoundException;
import com.jslhrd.yorimichi.mapper.FacilityCategoryMapper;
import com.jslhrd.yorimichi.mapper.StoreFacilityCategoryMapper;
import com.jslhrd.yorimichi.mapper.StoreMapper;
import com.jslhrd.yorimichi.service.FacilityCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FacilityCategoryManager implements FacilityCategoryService {

	private final FacilityCategoryMapper facilityCategoryMapper;
	private final StoreFacilityCategoryMapper storeFacilityCategoryMapper;
	private final StoreMapper storeMapper;

	@Override
	@Transactional(readOnly = true)
	public List<FacilityCategoryDTO> findAll() {
		return facilityCategoryMapper.selectAll();
	}

	@Override
	public Long getOrCreateByName(FacilityCategoryDTO facility) {

		String name = facility.getName();

		Long findFacilityId = facilityCategoryMapper.selectIdByName(name);
		if (findFacilityId != null) {
			return findFacilityId;
		}

		try {
			save(facility);
			return facility.getId();
		} catch (DuplicateKeyException e) {
			findFacilityId = facilityCategoryMapper.selectIdByName(name);
			if (findFacilityId != null) {
				return findFacilityId;
			}
			
			throw e;
		}
	}

	@Override
	public void save(FacilityCategoryDTO facility) {

		try {
			facilityCategoryMapper.insert(facility);
		} catch (DuplicateKeyException e) {
			throw new DuplicateFacilityCategoryException(facility.getName());
		}

		log.info("Facility: created facilityId={}", facility.getId());
	}

	@Override
	public void update(Long facilityId, FacilityCategoryDTO facility) {

		if (facility.getId() != null && !facilityId.equals(facility.getId())) {
			throw new BadRequestException("경로의 facilityId 와 본문의 id 가 다릅니다.");
		}

		try {
			boolean affected = facilityCategoryMapper.update(facilityId, facility) > 0;
			if (!affected) {
				assertExistFacilityCategory(facilityId);
				log.debug("Facility: update no-op facilityId={}, facility={}", facilityId, facility);
				return;
			}
		} catch (DuplicateKeyException e) {
			throw new DuplicateFacilityCategoryException(facility.getName());
		}

		log.info("Facility: updated facilityId={}", facilityId);
	}

	@Override
	public void delete(Long facilityId) {

		boolean affected = facilityCategoryMapper.deleteById(facilityId) > 0;
		if (!affected) {
			assertExistFacilityCategory(facilityId);
			log.debug("Facility: delete no-op facilityId={}", facilityId);
			return;
		}

		log.info("Facility: deleted facilityId={}", facilityId);
	}

	@Override
	public void addFacilityCategoryToStore(Long storeId, Long facilityId) {

		assertActiveStore(storeId);
		assertExistFacilityCategory(facilityId);

		try {
			storeFacilityCategoryMapper.insert(storeId, facilityId);
		} catch (DuplicateKeyException e) {
			log.debug("StoreFacilityCategory: add no-op storeId={}, facilityId={}", storeId, facilityId);
			return;
		}

		log.info("StoreFacilityCategory: add storeId={}, facilityId={}", storeId, facilityId);
	}


	@Override
	public void removeFacilityCategoryFromStore(Long storeId, Long facilityId) {

		boolean affected = storeFacilityCategoryMapper.delete(storeId, facilityId) > 0;
		if (!affected) {
			log.debug("StoreFacilityCategory: remove no-op storeId={}, facilityId={}", storeId, facilityId);
			return;
		}

		log.info("StoreFacilityCategory: removed storeId={}, facilityId={}", storeId, facilityId);
	}

	private void assertExistFacilityCategory(Long facilityId) {
		boolean exists = facilityCategoryMapper.existsById(facilityId);
		if (!exists) {
			throw new FacilityCategoryNotFoundException(facilityId);
		}
	}

	private void assertActiveStore(Long storeId) {
		boolean exists = storeMapper.existsActive(storeId);
		if (!exists) {
			throw new StoreNotFoundException(storeId);
		}
	}
}