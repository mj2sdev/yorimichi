package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.AddressDTO;
import com.jslhrd.yorimichi.mapper.AddressMapper;
import com.jslhrd.yorimichi.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressManager implements AddressService {

	private final AddressMapper addressMapper;

	@Override
	@Transactional
	public void upsert(AddressDTO address) {

		addressMapper.upsert(address);

		log.info("Address: created addressId={}", address.getId());
	}
}