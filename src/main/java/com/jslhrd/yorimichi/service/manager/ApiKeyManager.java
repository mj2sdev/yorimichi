package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.ApiKeyDTO;
import com.jslhrd.yorimichi.mapper.ApiKeyMapper;
import com.jslhrd.yorimichi.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiKeyManager implements ApiKeyService {

	private final ApiKeyMapper apiKeyMapper;

	@Override
	public String findApiKey(String keyName, String owner) {
		String name = Optional.ofNullable(keyName)
				.orElseThrow(() -> {
					log.debug("{} 키를 불러오는데 실패하였습니다.", keyName);
					return new IllegalArgumentException("아니 keyName 은 필수라니깐요?");
				});

		var dto = ApiKeyDTO.builder()
				.name(name)
				.owner(owner)
				.build();

		return apiKeyMapper.select(dto)
				.orElseThrow(() -> {
					log.debug("{} 키워드로 키를 찾을 수 없었습니다.", keyName);
					return new RuntimeException("apiKey를 찾을 수 없었습니다.");
				});
	}

	@Override
	public String findApiKey(String keyName) {
		return findApiKey(keyName, null);
	}
}
