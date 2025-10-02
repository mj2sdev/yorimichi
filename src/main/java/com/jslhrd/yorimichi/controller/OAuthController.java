package com.jslhrd.yorimichi.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

// @RestController
@RequiredArgsConstructor
public class OAuthController {

    private final GoogleAuthorizationCodeFlow flow;

    @Value("${google.oauth.redirect-uri}")
    private String redirectUri;

    /** 동의 화면으로 리다이렉트 */
    @GetMapping("/oauth2/authorize")
    public void authorize(HttpServletResponse resp) throws IOException {
        String url = flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .set("prompt", "consent") // 항상 refresh token 받도록
                .build();
        resp.sendRedirect(url);
    }

    /** 콜백: 토큰 저장 */
    @GetMapping("/oauth2/callback")
    public String callback(@RequestParam("code") String code) throws IOException {
        TokenResponse token = flow.newTokenRequest(code)
                .setRedirectUri(redirectUri)
                .execute();
        flow.createAndStoreCredential(token, "owner");
        return "로그인 완료! 이제 서버가 내 드라이브에 업로드할 수 있어요.";
    }
}
