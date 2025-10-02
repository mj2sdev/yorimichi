package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.service.GoogleDriveService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drive")
@Profile("service-drive")
public class GoogleDriveController {

    private final GoogleDriveService driveService; // 구현체 = GoogleDriveServiceManager

    /** 파일 업로드 → 공개 URL 반환 */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> upload(@RequestPart("file") MultipartFile file) {
        String url = driveService.uploadFile(file);  // manager가 공개 URL 반환하게 구현함
        return ResponseEntity.ok(Map.of("url", url));
    }

    @GetMapping("/ping")
    public String ping() { return "pong"; }
}
