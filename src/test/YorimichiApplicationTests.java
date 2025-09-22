package com.jslhrd.Yorimichi;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.mapper.UserMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@MapperScan("com.jslhrd.yorimichi.mapper") // 매퍼 인터페이스 패키지 스캔 확실히
@ActiveProfiles("test") // src/test/resources/application-test.yml 사용 (H2 + schema.sql)
class YorimichiApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Test
    @DisplayName("UserMapper: insert -> selectByEmail 스모크 테스트")
    void userMapper_insert_then_selectByEmail() {
        // given
        UserDTO u = new UserDTO();
        u.setRoleId(1L);
        u.setEmail("a@yorimichi.com");
        u.setPassword("pw");
        u.setNickname("alice");
        u.setDescription("desc");

        // when
        int inserted = userMapper.insert(u);
        UserDTO found = userMapper.selectByEmail("a@yorimichi.com");

        // then
        assertThat(inserted).isEqualTo(1);
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("a@yorimichi.com");
        assertThat(found.getNickname()).isEqualTo("alice");
        assertThat(found.getRoleId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("[디버그] 로딩된 MyBatis Statement 목록 출력")
    void _debug_printMappedStatements() {
        System.out.println("== mapped statements ==");
        sqlSessionFactory.getConfiguration().getMappedStatementNames()
                .forEach(System.out::println);

        // 핵심 스테이트먼트가 실제 로딩되었는지 간단 체크 (없으면 XML 스캔/네임스페이스 문제)
        assertThat(sqlSessionFactory.getConfiguration().getMappedStatementNames())
                .anyMatch(s -> s.equals("com.jslhrd.yorimichi.mapper.UserMapper.insert"));
        assertThat(sqlSessionFactory.getConfiguration().getMappedStatementNames())
                .anyMatch(s -> s.equals("com.jslhrd.yorimichi.mapper.UserMapper.selectByEmail"));
    }
}
