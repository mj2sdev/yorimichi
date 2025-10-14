package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class DuplicateNicknameException extends DomainException {
	public DuplicateNicknameException(String nickname) {
		super(
				"DUPLICATE_NICKNAME",
				"동일한 닉네임이 이미 등록되어 있습니다.",
				Map.of("nickname", nickname)
		);
	}
}
