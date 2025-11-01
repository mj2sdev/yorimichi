package com.jslhrd.yorimichi.service;

/**
 * API KEY를 블러오는데 도움을 주는 service 입니다.
 *
 * @author mj2sdev
 * @version 1.0 초안작성
 */
public interface ApiKeyService {

	/**
	 * api key의 이름을 입력하면 키를 불러오도록 합니다.
	 *
	 * @param keyName 필수임 (gemini, drive, ...)
	 * @param owner   선택임 (소유자 이름 입력, github Id)
	 * @return {@code String} apiKey
	 *
	 * <p>
	 * keyName 에는 가급적 식별 가능한 키워드를 입력하세요
	 * 목록 [google gemini, google drive] 이 존재하는데 keyName = google 이면
	 * 둘중 하나를 불러오기 때문에 불확실합니다.
	 * keyName = gemini 혹은 keyName = drive 등 겹치지 않고 식별 가능한 키워드를 입력하세요
	 * owner = mj2sdev 오너는 해당 키의 실제 소유자(발급자) 를 선택합니다.
	 * owner는 null 가능하며 키의 주인을 선택하는데 사용합니다.
	 */
	public String findApiKey(String keyName, String owner);

	public String findApiKey(String keyName);

}
