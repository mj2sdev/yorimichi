package com.jslhrd.yorimichi.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@Profile({"oauth-drive", "oauth-drive-only"})  // OAuth 켰을 때만 활성
@RequiredArgsConstructor
public class DriveOAuthConfig {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Value("classpath:secrets/oauth-client.json")
    private Resource clientSecretJson;

    @Value("${google.oauth.redirect-uri}")
    private String redirectUri;

    @Bean
    public NetHttpTransport httpTransport() throws Exception {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    public GoogleAuthorizationCodeFlow googleAuthFlow(NetHttpTransport http) throws Exception {
        GoogleClientSecrets secrets = GoogleClientSecrets.load(
                JSON_FACTORY,
                new InputStreamReader(clientSecretJson.getInputStream(), StandardCharsets.UTF_8)
        );

        return new GoogleAuthorizationCodeFlow.Builder(
                http, JSON_FACTORY, secrets, List.of(DriveScopes.DRIVE) // 필요 시 범위 축소 가능
        )
        .setAccessType("offline")
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens"))) // refresh token 저장 위치
        .build();
    }

    /** 사용자 OAuth 자격증명으로 Drive 클라이언트 구성 */
    @Bean
    public Drive googleDrive(NetHttpTransport http, GoogleAuthorizationCodeFlow flow) throws Exception {
        // "owner" 라는 ID로 토큰 로드 (최초 1회 로그인 필요)
        Credential cred = flow.loadCredential("owner");
        if (cred == null) {
            throw new IllegalStateException("아직 구글 로그인/동의가 없습니다. http://localhost:8080/oauth2/authorize 로 먼저 접속하세요.");
        }
        return new Drive.Builder(http, JSON_FACTORY, cred)
                .setApplicationName("yorimichi")
                .build();
    }
}
