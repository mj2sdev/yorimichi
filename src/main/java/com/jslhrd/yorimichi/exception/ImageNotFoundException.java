package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class ImageNotFoundException extends DomainException {

	public ImageNotFoundException(Long imageId) {
		super(
				"IMAGE_NOT_FOUND",
				"이미지를 찾을 수 없습니다.",
				Map.of("imageId", imageId)
		);
	}
}
