본 파일은 gemini의 도움을 받아 작성된 파일입니다.

## 선행조건
1. Java 11버전 이상
> java -version
2. Gradle 7.0 버전 이상
> gradle/wrapper/wrapper.properties의 distributionUrl을 확인할 것.
3. Google Cloud Project를 만들것
> 프로젝트당 한 사람만 만들면 되는 것 같습니다.
4. 구글 드라이브 사용이 가능한 구글 계정일 것.
 
---
 
## 설정 절차
 
### 1. Google Cloud 프로젝트 생성 및 API 활성화
 
1.  **프로젝트 생성**: Google Cloud Console에 접속하여 새 프로젝트를 생성합니다.
    *   `Project Name` 필드에 프로젝트를 설명하는 이름을 입력합니다. (예: `yorimichi-project`)
    *   `조직`은 별도로 선택하지 않아도 됩니다.
    *   `만들기`를 클릭합니다.
 
2.  **Google Drive API 활성화**:
    *   방금 만든 프로젝트의 대시보드에서 `API 및 서비스` > `라이브러리` 메뉴로 이동합니다.
    *   `Google Drive API`를 검색하여 선택하고 `사용 설정` 버튼을 클릭합니다.
 
### 2. 서비스 계정(Service Account) 생성 및 키 발급
 
서비스 계정은 우리 서버(애플리케이션)가 사용자의 개입 없이 Google Drive에 접근하기 위해 사용하는 '로봇 계정'입니다.
 
1.  **서비스 계정 생성**:
    *   `API 및 서비스` > `사용자 인증 정보` 메뉴로 이동합니다.
    *   `+ 사용자 인증 정보 만들기` > `서비스 계정`을 선택합니다.
    *   서비스 계정 이름(예: `yorimichi-image-uploader`)을 입력하고 `만들기 및 계속`을 클릭합니다.
    *   `역할 선택` 단계는 건너뛰고 `계속`을 클릭합니다.
    *   `사용자에게 이 서비스 계정에 대한 액세스 권한 부여` 단계도 건너뛰고 `완료`를 클릭합니다.
 
2.  **서비스 계정 이메일 복사**:
    *   `사용자 인증 정보` 목록에 방금 만든 서비스 계정이 나타납니다. 이 계정의 이메일 주소(예: `...gserviceaccount.com`)를 복사해 둡니다. 이 주소는 다음 단계에서 필요합니다.
 
3.  **JSON 키 발급**:
    *   방금 만든 서비스 계정 이메일을 클릭하여 상세 정보로 들어갑니다.
    *   `키` 탭으로 이동합니다.
    *   `키 추가` > `새 키 만들기`를 선택합니다.
    *   키 유형으로 **JSON**을 선택하고 `만들기`를 클릭합니다.
    *   JSON 파일이 자동으로 다운로드됩니다. 이 파일은 **매우 중요한 비밀 키**이므로 안전하게 보관해야 하며, Git에 커밋하는 등 외부에 노출되지 않도록 주의해야 합니다.
    *   다운로드된 JSON 파일을 Spring Boot 프로젝트의 `src/main/resources` 폴더에 저장합니다.
 
### 3. Google Drive 폴더 준비 및 공유
 
이제 서비스 계정이 파일을 업로드할 Google Drive 내의 특정 폴더를 지정하고 권한을 부여합니다.
 
1.  **폴더 생성**: Google Drive에서 이미지를 저장할 폴더를 하나 생성합니다(예: `yorimichi-images`).
 
2.  **폴더 공유**:
    *   생성한 폴더를 마우스 오른쪽 버튼으로 클릭하고 `공유`를 선택합니다.
    *   `사용자 및 그룹 추가` 입력란에 위에서 복사해 둔 **서비스 계정의 이메일 주소**를 붙여넣습니다.
    *   권한을 **'편집자(Editor)'**로 설정하고 `보내기`를 클릭합니다. (서비스 계정이 이 폴더에 파일을 업로드해야 하므로 '편집자' 권한이 필요합니다.)
 
3.  **폴더 ID 확인**:
    *   해당 폴더를 열었을 때 브라우저 주소창의 URL을 확인합니다.
    *   URL이 `https://drive.google.com/drive/folders/11Yul8WMC-27LRVtWOwNw1UuS50WfM-5_` 와 같은 형식일 경우, `folders/` 뒤의 `11Yul8WMC-27LRVtWOwNw1UuS50WfM-5_` 부분이 바로 **폴더 ID**입니다. 이 ID는 나중에 코드에서 사용됩니다.

---

## 4. 서버 구현 (Spring Boot)

사전 준비가 완료되었으면, 이제 Spring Boot 애플리케이션에 실제 코드를 구현합니다.

### 4.1. 의존성 추가 (`build.gradle`)

`build.gradle` 파일의 `dependencies` 블록에 Google API 클라이언트 라이브러리를 추가합니다.

```groovy
dependencies {
    // ... 기존 의존성들
    implementation 'com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0'
    implementation 'com.google.oauth-client:google-oauth-client-jetty:1.34.1'
}
```

### 4.2. 설정 파일 구성 (`application.properties`)

코드에 직접 값을 넣는 대신, `src/main/resources/application.properties` 파일에 Google Drive 관련 설정 값을 추가하여 관리합니다. 이렇게 하면 나중에 값을 변경하기 쉽습니다.

```properties
# ===============================
# Google Drive API Settings
# ===============================
google.drive.credentials.path=classpath:your-service-account-key.json
google.drive.folder-id=YOUR_GOOGLE_DRIVE_FOLDER_ID
```
*   `google.drive.credentials.path`: 2단계에서 다운로드한 JSON 키 파일의 경로입니다. `classpath:`는 `src/main/resources` 폴더를 가리킵니다. 파일 이름을 실제 파일명으로 변경해주세요.
*   `google.drive.folder-id`: 3단계에서 확인한 Google Drive 폴더의 ID입니다.

### 4.3. Java 코드 구현 (Service)

이미지 업로드를 처리할 `GoogleDriveService`를 생성합니다. 아래는 전체 코드 예시입니다. 프로젝트의 패키지 구조에 맞게 `com.jslhrd.yorimichi.service` 부분을 수정하여 사용하세요.

**`GoogleDriveService.java`**
```java
package com.jslhrd.yorimichi.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

@Slf4j
@Service
public class GoogleDriveService {

    @Value("${google.drive.credentials.path}")
    private Resource credentialsResource;

    @Value("${google.drive.folder-id}")
    private String folderId;

    private static final String APPLICATION_NAME = "Yorimichi";

    public String uploadImageAndGetUrl(String imageUrlFromSource, String imageName) {
        Path tempFile = null;
        try {
            Drive driveService = getDriveService();

            log.info("Downloading image from: {}", imageUrlFromSource);
            tempFile = Files.createTempFile("upload-", ".jpg");
            try (InputStream in = new URL(imageUrlFromSource).openStream()) {
                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            log.info("Uploading file '{}' to Google Drive Folder ID: {}", imageName, folderId);
            File fileMetadata = new File();
            fileMetadata.setName(imageName);
            fileMetadata.setParents(Collections.singletonList(folderId));

            FileContent mediaContent = new FileContent("image/jpeg", tempFile.toFile());
            File uploadedFile = driveService.files().create(fileMetadata, mediaContent).setFields("id").execute();
            String fileId = uploadedFile.getId();
            log.info("File uploaded successfully. File ID: {}", fileId);

            Permission permission = new Permission().setType("anyone").setRole("reader");
            driveService.permissions().create(fileId, permission).execute();
            log.info("Permission set to 'anyone with the link can view'.");

            return "https://lh3.googleusercontent.com/d/" + fileId;

        } catch (Exception e) {
            log.error("Failed to upload image to Google Drive", e);
            throw new RuntimeException("Google Drive 이미지 업로드 실패", e);
        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                    log.info("Temporary file deleted: {}", tempFile.toString());
                } catch (Exception e) {
                    log.error("Failed to delete temporary file: {}", tempFile.toString(), e);
                }
            }
        }
    }

    private Drive getDriveService() throws Exception {
        try (InputStream credentialsStream = credentialsResource.getInputStream()) {
            GoogleCredential credential = GoogleCredential.fromStream(credentialsStream)
                    .createScoped(Collections.singleton(DriveScopes.DRIVE));
            return new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                    .setApplicationName(APPLICATION_NAME).build();
        }
    }
}
```


# Google Drive API 연동 가이드: 공식 문서 분석 및 학습 로드맵

이 문서는 Spring Boot와 같은 서버 애플리케이션에서 Google Drive API를 연동하는 방법을 배우기 위해 참조해야 할 핵심 공식 문서들을 분석하고, 체계적인 학습 로드맵을 제시합니다.

---

## 1. 참조 공식 문서 분석

성공적인 API 연동 코드는 여러 공식 문서를 조합하여 만들어집니다. 핵심이 되는 문서는 다음과 같습니다.

### 1.1. Google Drive API (v3) Documentation

Google Drive의 모든 기능과 상호작용하는 방법의 기초가 되는 문서입니다.

* **참조 주소**: [Google Drive API Documentation](https://developers.google.com/drive/api/v3/about-sdk)
* **핵심 참조 부분**:
    * **`Files: create` (파일 업로드)**: 파일의 메타데이터(이름, 부모 폴더)와 실제 콘텐츠를 서버로 전송하는 방법을 설명합니다. Java 코드의 `driveService.files().create(...)` 메소드가 이 문서를 기반으로 합니다.
        * [파일 업로드 가이드 바로가기](https://developers.google.com/drive/api/guides/manage-uploads)
    * **`Permissions: create` (권한 설정)**: 업로드된 파일을 웹에서 접근할 수 있도록 권한을 부여하는 방법을 다룹니다. `anyone` (누구나) / `reader` (읽기 권한) 등의 역할 설정이 핵심입니다.
        * [파일 공유 가이드 바로가기](https://developers.google.com/drive/api/guides/share)
    * **Java Quickstart**: Java 환경에서 API 연동을 시작하는 가장 기초적인 예제입니다. 인증 객체 생성 및 API 호출의 기본 구조를 여기서 파악할 수 있습니다.
        * [Java 빠른 시작 가이드 바로가기](https://developers.google.com/drive/api/quickstart/java)

---

### 1.2. Google Cloud IAM (Identity and Access Management)

서버가 사용자의 개입 없이 Google 서비스에 안전하게 접근하기 위한 '서비스 계정'의 모든 것을 다룹니다.

* **참조 주소**: [Google Cloud IAM Documentation](https://cloud.google.com/iam/docs)
* **핵심 참조 부분**:
    * **서비스 계정(Service Accounts) 개요**: 서비스 계정이란 무엇이며, 왜 서버 간 인증에 필요한지에 대한 개념을 설명합니다.
        * [서비스 계정 개요 바로가기](https://cloud.google.com/iam/docs/service-account-overview)
    * **서비스 계정 키 생성 및 관리**: 가이드의 'JSON 키 발급' 절차가 이 문서를 직접적으로 참조한 것입니다. JSON 키 파일의 역할과 보안의 중요성을 강조합니다.

---

### 1.3. Google API Client Library for Java

Java 코드에서 Google API를 쉽게 호출할 수 있도록 도와주는 라이브러리 문서입니다.

* **참조 주소**: [Google API Client Library for Java (GitHub)](https://github.com/googleapis/google-api-java-client)
* **핵심 참조 부분**:
    * **`GoogleCredential` 클래스**: 서비스 계정의 JSON 파일을 읽어 인증 정보를 생성하는 방법을 설명합니다. `GoogleCredential.fromStream(...)` 코드가 이 라이브러리 사용법을 따른 것입니다.
    * **서비스 빌더 (`Drive.Builder`)**: 인증 정보를 사용하여 실제 API 요청을 보낼 `Drive` 서비스 객체를 생성하는 방법을 안내합니다.

---

## 2. 학습 로드맵: 전문가가 되는 길 🗺️

문서만 읽는 것보다, 명확한 목표를 가지고 직접 코드를 실행하며 배우는 것이 효과적입니다.

#### **1단계: 큰 그림 이해하기 (개념 학습)**

"내 서버가 어떻게 내 구글 드라이브에 자동으로 파일을 올릴 수 있을까?"라는 질문으로 시작하세요. 이 질문은 자연스럽게 **API**, **인증(Authentication)**, 그리고 서버용 로봇 계정인 **서비스 계정(Service Account)**의 필요성으로 이어집니다.

#### **2단계: 가장 작은 단위로 시작하기 (Quickstart 따라하기)**

공식 문서의 **'Quickstart'**는 최고의 교재입니다. [Google Drive API Java Quickstart](https://developers.google.com/drive/api/quickstart/java)를 수정 없이 그대로 실행하여, 내 드라이브의 파일 목록이 출력되는 것을 직접 확인하는 경험이 매우 중요합니다.

#### **3단계: 목표에 맞게 수정하기 (응용)**

Quickstart 코드를 '파일 목록 조회'에서 '파일 업로드' 기능으로 수정해 보세요. `Files: create` 공식 문서의 예제 코드를 가져와 적용하며 발생하는 오류를 해결하는 과정에서 진짜 실력이 향상됩니다. "아, 부모 폴더 ID를 지정해야 하는구나"와 같은 깨달음을 얻게 됩니다.

#### **4단계: 더 나은 구조로 만들기 (리팩토링)**

코드가 정상적으로 동작하면, 이제 더 세련되게 다듬을 차례입니다. 코드에 하드코딩된 폴더 ID나 파일 경로를 `application.properties` 같은 설정 파일로 분리하세요. 이 과정에서 "Spring Boot에서 설정 파일 값 읽어오기" 등을 검색하며 `@Value` 어노테이션과 같은 프레임워크의 기능을 자연스럽게 익히게 됩니다.

> **핵심**: 전체를 한 번에 이해하려 하지 말고, 동작하는 가장 작은 예제에서 시작해 내가 원하는 기능으로 조금씩 확장해 나가는 것이 가장 빠른 길입니다.