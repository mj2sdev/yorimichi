package com.jslhrd.yorimichi.config;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.jslhrd.yorimichi.service.ApiKeyService;

import lombok.RequiredArgsConstructor;

/**
 * @author GeonHoKoo
 * @version 1.0
 * 초안 작성
 * 
 * @author mj2sdev
 * @version 1.1
 * 1. 구글 드라이브 API 공식 문서를 참고하여 최신버전으로 수정
 * -> 이전 버전은 직접 controller 를 구현하여 인증값을 수동으로 저장
 * 현재 버전은 구글에서 제공되는 reciever를 사용하여 8888 포트로 수신하여 token값을 저장
 * 2. API KEY 누락 시 서버 정상 가동되도록 수정
 */
@Configuration
@DependsOn("dataSource")
@RequiredArgsConstructor
public class GoogleDriveConfig {

	private final ApiKeyService apiKeyService;

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("${google.drive.token-path}")
	private String tokenPath;

	@Value("${google.drive.credentials-file-path}")
	private String credentialsFilepath;

	@Bean
	public JsonFactory jsonFactory() {
		return GsonFactory.getDefaultInstance();
	}

	@Bean
	public NetHttpTransport gNetHttpTransport() throws IOException, GeneralSecurityException {
		return GoogleNetHttpTransport.newTrustedTransport();
	}

	public Credential getCredentials(final NetHttpTransport httpTransport, final JsonFactory jsonFactory) throws IOException {
		// InputStream in = GoogleDriveConfig.class.getResourceAsStream(credentialsFilepath);

		String credentialsJson = apiKeyService.findApiKey("drive", null);
		
		InputStream in = new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8));

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

		List<String> scopes = Collections.singletonList(DriveScopes.DRIVE_FILE);
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes)
			.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokenPath)))
			.setAccessType("offline")
			.build();

		LocalServerReceiver receiver = new LocalServerReceiver.Builder()
			.setPort(8888)
			.build();

		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	@Bean
	public Drive getDrive(NetHttpTransport httpTransport, JsonFactory jsonFactory) 
		throws IOException, GeneralSecurityException {
		return new Drive.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
			.setApplicationName(applicationName)
			.build();
	}
}
