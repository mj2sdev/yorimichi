package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.service.GoogleDriveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

// @RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileTestController {

    private final GoogleDriveService googleDriveService;

    /**
     * 업로드 테스트용 엔드포인트
     * - form-data: key = "file"
     * - 응답: { "url": "https://lh3.googleusercontent.com/d/<fileId>" }
     */
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        String url = googleDriveService.uploadFile(file);
        return ResponseEntity.ok(Map.of("url", url));
    }

    // 선택: 서버 살아있는지 체크
    @GetMapping(value = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    public String ping() {
        return "ok";
    }
}
