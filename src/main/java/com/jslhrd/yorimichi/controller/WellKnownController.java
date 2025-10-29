package com.jslhrd.yorimichi.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 204 응답 컨트롤러
@RestController
class WellKnownController {
	@GetMapping("/.well-known/appspecific/com.chrome.devtools.json")
	public ResponseEntity<Void> devtoolsJson() {
		return ResponseEntity.noContent().build(); // 204 No Content → WARN 안 뜸
	}
}