# 🧪 YorimichiApplicationTests 사용 가이드

이 문서는 `YorimichiApplicationTests.java`를 처음 접하는 사람도 쉽게 따라와서  
**MyBatis XML Mapper 테스트**를 진행할 수 있도록 작성된 가이드입니다.  

---

## 📌 개요
- **목적**: Mapper 인터페이스 ↔ XML 매핑이 올바르게 동작하는지 검증
- **환경**: H2 인메모리 DB + `schema.sql` + Spring Boot `@MybatisTest`
- **결과**: 모든 매퍼가 insert/select/update/delete 기본 동작을 하는지 스모크 테스트

---

## ⚙️ 환경 구성
- Spring Boot 3.x
- JUnit 5 (Jupiter)
- MyBatis Spring Boot Starter
- H2 Database
- AssertJ

테스트 실행 시 사용하는 설정 파일: src/test/resources/application-test.yml

→ H2 인메모리 DB + `schema.sql` 로 스키마 자동 로드.

---

## 🗂️ 준비
1. **`schema.sql` 확인**  
   - H2 호환 테이블 정의 필요  
   - `created_at`/`updated_at`은 DB에서 자동 관리  

2. **Mapper 네임스페이스 규칙**  
   - 네임스페이스 = 인터페이스 FQCN  
     예) `com.jslhrd.yorimichi.mapper.UserMapper`  

3. **XML 규칙**  
   - `COUNT(*)` 통일  
   - `update`에서는 `created_at` 제외  
   - `ORDER BY` ASC/DESC 명시  

---

## 🧩 테스트 코드 기본 구조
`YorimichiApplicationTests.java`는 다음과 같은 형태로 작성됩니다:

```java
@MybatisTest
@MapperScan("com.jslhrd.yorimichi.mapper")
@ActiveProfiles("test")
class YorimichiApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Test
    @DisplayName("UserMapper: insert -> selectByEmail 스모크 테스트")
    void userMapper_insert_then_selectByEmail() {
        UserDTO u = new UserDTO();
        u.setRoleId(1L);
        u.setEmail("a@yorimichi.com");
        u.setPassword("pw");
        u.setNickname("alice");

        int inserted = userMapper.insert(u);
        UserDTO found = userMapper.selectByEmail("a@yorimichi.com");

        assertThat(inserted).isEqualTo(1);
        assertThat(found).isNotNull();
        assertThat(found.getNickname()).isEqualTo("alice");
    }
}

## 🛠️ 매퍼별 테스트 추가 방법

1. 원하는 Mapper 인터페이스를 `@Autowired`로 주입합니다.

   ```java
   @Autowired
   AddressMapper addressMapper;
2. 스모크 테스트 메서드를 작성합니다.
(보통 insert → selectById 조합으로 최소 동작 확인)
@Test
@DisplayName("AddressMapper: insert -> selectById 스모크 테스트")
void addressMapper_insert_then_selectById() {
    AddressDTO a = new AddressDTO();
    a.setUserId(1L);
    a.setRoad("테스트 도로");
    a.setDetail("상세");
    a.setZipcode("12345");
    a.setIsDefault(true);

    int inserted = addressMapper.insert(a);
    AddressDTO found = addressMapper.selectById(a.getId());

    assertThat(inserted).isEqualTo(1);
    assertThat(found).isNotNull();
    assertThat(found.getRoad()).isEqualTo("테스트 도로");
}

3. 이런 방식으로 알파벳 순서대로 Mapper 테스트를 하나씩 추가하면,
전체 XML 매핑 검증이 가능합니다.

## 🔍 디버그 팁

XML 매퍼가 스캔되지 않거나 실행 에러가 날 때는
SqlSessionFactory에서 로딩된 Statement 목록을 확인합니다.

@Test
@DisplayName("[디버그] 로딩된 MyBatis Statement 목록 출력")
void _debug_printMappedStatements() {
    sqlSessionFactory.getConfiguration().getMappedStatementNames()
            .forEach(System.out::println);

    assertThat(sqlSessionFactory.getConfiguration().getMappedStatementNames())
            .anyMatch(s -> s.equals("com.jslhrd.yorimichi.mapper.UserMapper.insert"));
}

## ✅ 요약
-YorimichiApplicationTests는 Mapper 스모크 테스트 허브

- schema.sql은 H2용 전체 테이블 구조 포함

- 새 매퍼 테스트 추가는 UserMapper 예시 복사 → DTO 필수 필드만 채워주면 됨

- 에러 발생 시 디버그 출력으로 XML 매핑 상태 확인