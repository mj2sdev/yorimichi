## Windows 개발 환경 설정 가이드

### 1. Java JDK 21 설치
VS Code는 JDK를 포함하고 있지 않으므로, 필요한 버전의 JDK를 직접 설치해야 합니다.
- [Java 21 다운로드 링크](https://www.oracle.com/java/technologies/downloads/#java21)
- [Java 21 공식 문서](https://docs.oracle.com/en/java/javase/21/) *(관심 있는 분만 열어보세요)*

### 2. Visual Studio Code (VS Code) 설치
아래 링크에 접속하여 `Download for Windows` 버튼을 클릭해 설치합니다.
- [VS Code 공식 홈페이지](https://code.visualstudio.com/)

### 3. 데이터베이스 (MySQL)
- 개발에는 `MySQL`을 사용합니다. (설치 필요)

### 4. Git 설치
버전 관리를 위해 Git을 설치합니다.
- [Git 다운로드 (Windows)](https://git-scm.com/downloads)
- [Git 공식 문서](https://git-scm.com/doc)

---

## VS Code 확장 프로그램 (Extensions) 설치

> 
> 좌측의 확장 프로그램 탭에서 아래 항목들을 검색하여 설치합니다.

1.  **Extension Pack for Java**
    > 자바 개발 환경을 구성해주는 필수 확장 기능 모음입니다.

2.  **Spring Boot Extension Pack**
    > 스프링 부트 개발을 편리하게 해주는 확장 기능 모음입니다.

---

## 프로젝트 설정 및 실행

### Spring Boot 프로젝트 생성 (연습용)
1.  `Ctrl` + `Shift` + `P`를 눌러 명령어 팔레트를 엽니다.
2.  `Spring Initializr: Create a Gradle Project`를 선택합니다.
3.  아래 표를 참고하여 프로젝트 설정을 진행합니다.

| 설정 항목 | 값 |
| :--- | :--- |
| Spring Boot Version | 3.5.5 (또는 최신) |
| Language | Java |
| Group | com.jslhrd |
| Artifact | yorimichi |
| Packaging | Jar |
| Java Version | 21 |

4.  **Dependencies (의존성) 선택**:
    - `Spring Web`: 스프링으로 웹 애플리케이션을 만들 때 필요합니다.
    - `Thymeleaf`: 템플릿 엔진입니다. ([Thymeleaf 공식](https://www.thymeleaf.org/), [Spring 연동 공식](https://www.thymeleaf.org/doc/articles/thymeleafspringboot.html))
    - `Lombok`: 반복적인 코드를 줄여주는 라이브러리입니다.
    - `MyBatis Framework`: SQL 매퍼 프레임워크입니다.
    - `MySQL Driver`: MySQL 데이터베이스와 연동하기 위한 드라이버입니다.
    - `Spring Boot DevTools`: 코드 변경 시 자동으로 재시작해주는 개발 도구입니다.
    - `Spring Security`: 보안 설정을 위한 프레임워크입니다. (끼얏호 보안!)

### 프로젝트 클론 (첫 동기화 작업)
- 터미널에서 아래 명령어를 실행하여 프로젝트를 복제합니다.
```bash
git clone https://github.com/mj2sdev/yorimichi

```

## 유용한 추가 정보

### 추천 VS Code 확장 프로그램
- [**Auto Rename Tag**](vscode:extension/formulahendry.auto-rename-tag): 여는 태그를 수정하면 닫는 태그도 자동으로 수정해줍니다.
- [**Live Server**](vscode:extension/ritwickdey.LiveServer): 간단한 로컬 서버를 실행하여 정적 웹 페이지를 확인할 수 있습니다.
- [**Markdown Diagram**](vscode:extension/skyer.vscode-markdown-diagram): markdown 다이어그램 문법을 vscode에서도 볼 수 있도록 해줍니다. (GitHub는 바로 보임)
- [**Markdown Obsidian Callout**](vscode:extension/TendouAlice.markdown-obsidian-callout): markdown 에서의 문법 중 하나인 callout 관련 기능을 표시해 줍니다. 
- [**GitLens**](vscode:extension/eamodio.gitlens): vscode 에서 git관련 정보를 좀더 자세히 표시해줍니다.
- [**Database Client JDBC**](vscode:extension/cweijan.dbclient-jdbc): mysql, mariadb 관련 데이터베이스 UI 접근 툴 입니다. 진짜 편해요 ^


### Gradle 의존성 버전 확인
- 터미널에서 아래 명령어를 사용해 설치된 의존성의 버전을 확인할 수 있습니다.
```bash
./gradlew.bat dependencyInsight --dependency [의존성 이름]