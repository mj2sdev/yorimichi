package com.jslhrd.yorimichi.controller;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// @RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class DriveListTestController {

    private final Drive drive;

    @Value("${google.drive.folder-id}")
    private String folderId;

    @GetMapping("/list")
    public List<Map<String, Object>> list() throws IOException {
        String q = "'" + folderId + "' in parents and trashed = false";

        FileList result = drive.files().list()
                .setQ(q)
                .setFields("files(id,name,mimeType,size,modifiedTime)") // ✅ 파라미터 라벨 없이 문자열만
                // 공유 드라이브에서 조회할 때만 아래 두 줄을 켜세요
                // .setIncludeItemsFromAllDrives(true)
                // .setSupportsAllDrives(true)
                .execute();

        return result.getFiles().stream()
                .map(f -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", f.getId());
                    m.put("name", f.getName());
                    m.put("mimeType", f.getMimeType());
                    m.put("size", f.getSize());
                    m.put("modifiedTime", f.getModifiedTime());
                    // 드라이브 보기 링크
                    m.put("webView", "https://drive.google.com/file/d/" + f.getId() + "/view");
                    // 공개(또는 링크 공개)일 때 이미지 직접 표시 가능한 링크
                    m.put("publicContent", "https://lh3.googleusercontent.com/d/" + f.getId());
                    return m;
                })
                .toList(); // JDK 16+ (JDK 8이면 Collectors.toList())
    }
}
