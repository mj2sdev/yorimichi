package com.jslhrd.yorimichi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 구글 드라이브 설정 바인딩.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "google.drive")
public class GoogleDriveProperties {
    private String credentialsPath;     // 서비스계정 JSON 경로 (또는 환경변수 사용)
    private String applicationName = "yorimichi";
    private String folderId;            // 업로드 대상 폴더 ID(없으면 My Drive 루트)
    private boolean makePublic = true;  // 업로드 직후 공개(링크로 보기)
    private boolean supportsAllDrives = false; // 공유드라이브 사용 시 true
    private String linkType = "uc";     // uc | webView | webContent
}
