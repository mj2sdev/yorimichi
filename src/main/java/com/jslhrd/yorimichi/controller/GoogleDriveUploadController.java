package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.service.GoogleDriveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
// @RestController
@RequiredArgsConstructor
@RequestMapping("/api/drive")
public class GoogleDriveUploadController {

    private final GoogleDriveService driveService;

    /**
     * 단건 업로드: test-upload.html에서 FormData에 "file"로 보냄
     */
    @PostMapping(
        value = "/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> upload(@RequestPart("file") MultipartFile file) {
        log.info("single upload: {}", file.getOriginalFilename());
        String url = driveService.uploadFile(file);
        return ResponseEntity.ok(url); // 예: https://lh3.googleusercontent.com/d/{fileId}
    }

    /**
     * 다건 업로드: test-upload.html에서 FormData에 "files"로 보냄
     * GoogleDriveService#uploadFiles(List<MultipartFile>) 사용
     */
    @PostMapping(
        value = "/upload-many",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<String>> uploadMany(@RequestPart("files") List<MultipartFile> files) {
        log.info("multi upload: {} files", files.size());
        List<String> urls = driveService.uploadFiles(files);
        return ResponseEntity.ok(urls); // 예: ["https://lh3.googleusercontent.com/d/{id1}", ...]
    }
}
