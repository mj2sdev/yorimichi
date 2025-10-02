package com.jslhrd.yorimichi.config;

import com.jslhrd.yorimichi.service.social.SocialAuthClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.lang.reflect.Proxy;

/**
 * Drive 업로드 로컬 테스트용: SocialAuthClient가 없어 컨텍스트가 깨지지 않도록
 * 동적 프록시로 "아무 동작도 하지 않는" Stub 빈을 등록한다.
 */
@Configuration
@Profile("local-drive-test")
public class LocalDriveTestConfig {

    @Bean
    public SocialAuthClient socialAuthClientStub() {
        // 인터페이스의 모든 메서드 호출에 대해 null 반환 (no-op)
        return (SocialAuthClient) Proxy.newProxyInstance(
                SocialAuthClient.class.getClassLoader(),
                new Class[]{SocialAuthClient.class},
                (proxy, method, args) -> null
        );
    }
}
