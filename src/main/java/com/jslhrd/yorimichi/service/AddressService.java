package com.jslhrd.yorimichi.service;


import com.jslhrd.yorimichi.domain.AddressDTO;

public interface AddressService {

	void upsert(AddressDTO address);
}
