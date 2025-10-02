package com.jslhrd.yorimichi.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
@Profile("service-drive")
public class DriveConfig {

    // 앱 이름은 yml에서 오버라이드 가능 (없으면 기본값 Yorimichi)
    @Value("${google.drive.application-name:Yorimichi}")
    private String applicationName;

    /**
     * 클래스패스(resources)에서 서비스 계정 키 JSON을 읽어
     * Google Drive 클라이언트(Drive)를 생성한다.
     *
     * 전제:
     * - 파일 경로: src/main/resources/secrets/yorimichi-gdrive.json
     * - GCP에서 Drive API가 Enable 상태여야 함
     * - 업로드 대상 폴더가 서비스 계정 이메일(@*.gserviceaccount.com)에게 편집 권한 공유되어 있어야 함
     */
    @Bean
    public Drive googleDrive() {
        try (InputStream in = new ClassPathResource("secrets/yorimichi-gdrive.json").getInputStream()) {

            GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                    .createScoped(Collections.singleton(DriveScopes.DRIVE));

            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

            return new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    requestInitializer
            )
            .setApplicationName(applicationName)
            .build();

        } catch (IOException | GeneralSecurityException e) {
            // 자격증명 파일 누락/손상, 권한 문제 등
            throw new IllegalStateException(
                    "Failed to create Google Drive client. " +
                    "Make sure secrets/yorimichi-gdrive.json exists on the classpath.", e
            );
        }
    }
}
