
# Gemini API 사용 가이드

## 1. Gemini API에 대해
Gemini API는 구글에서 제공하는 생성형 AI API로, 텍스트, 이미지, 오디오 등 다양한 입력을 처리하고 응답을 생성할 수 있습니다.
- 특징: 고품질 생성, 다양한 멀티모달 입력 지원, Free Tier 제공

## 2. 사용 목적
- 개인 학습 및 개발용
- 앱에서 간단한 대화 기능 구현
- 텍스트 생성, 요약, 번역 등 실험적 기능 테스트

## 3. 사용 방법 (Java)

### 3.1 환경 준비
- JDK 17 이상 권장
- Maven 또는 Gradle 프로젝트 설정
- 필요 라이브러리: HTTP 클라이언트, JSON 파서

### 3.2 인증(Authentication)
- Google Cloud에서 API Key 발급
- 코드에서 HTTP 헤더에 API Key 추가

### 3.3 기본 호출 예제
```java
import java.net.http.*;
import java.net.URI;

public class GeminiExample {
  public static void main(String[] args) throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://api.generativeai.googleapis.com/v1beta2/models/gemini-2.5:generateText"))
      .header("Authorization", "Bearer YOUR_API_KEY")
      .POST(HttpRequest.BodyPublishers.ofString("{\"prompt\":\"안녕, Gemini!\"}"))
      .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println(response.body());
  }
}
```

### 3.4 오류 처리
- API 호출 실패 시 예외 처리
- rate limit 초과, 네트워크 오류 등에 대비

## 4. 요금
- **무료 티어**: 월별 한도 내에서 무료 호출 가능
- 유료 요금: 토큰 단위 과금, 모델별 요금 상이
- 배치 모드 사용 시 요금 절약 가능

## 5. 기타 주의사항
- 무료 티어 한도 초과 시 요청 실패
- 동시 호출 제한 있음
- 일부 기능은 무료 티어에서 제한됨
- API Key 유출 주의
