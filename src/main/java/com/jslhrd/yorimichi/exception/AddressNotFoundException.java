package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AddressNotFoundException extends DomainException {

	public AddressNotFoundException(Long addressId) {
		super(
				"ADDRESS_NOT_FOUND",
				"주소를 찾을 수 없습니다.",
				Map.of("addressId", addressId));
	}
}
