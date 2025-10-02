package com.jslhrd.yorimichi.manager;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.jslhrd.yorimichi.service.GoogleDriveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Slf4j
// @Service
@RequiredArgsConstructor
public class GoogleDriveServiceManager implements GoogleDriveService {

    private final Drive drive;

    /** 업로드 폴더(선택) */
    @Value("${google.drive.upload-folder-id:}")
    private String uploadFolderId;

    private static String contentUrl(String fileId) {
        return "https://lh3.googleusercontent.com/d/" + fileId;
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        List<String> urls = new ArrayList<>();
        List<String> createdIds = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                String id = uploadOneAndMakePublic(file);
                createdIds.add(id);
                urls.add(contentUrl(id));
            }
            return urls;
        } catch (Exception e) {
            // 베스트에포트 롤백: 이미 올라간 파일 삭제
            for (String id : createdIds) {
                try { drive.files().delete(id).execute(); }
                catch (Exception ignore) { /* no-op */ }
            }
            log.error("Google Drive 일괄 업로드 실패: {}", e.getMessage(), e);
            throw new RuntimeException("Google Drive 업로드 실패(일괄 처리 취소됨)", e);
        }
    }

    /** 파일 하나 업로드 + '링크 있는 모든 사용자 읽기' 권한 부여, 파일 ID 반환 */
    private String uploadOneAndMakePublic(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 업로드할 수 없습니다.");
        }

        String contentType = (file.getContentType() != null) ? file.getContentType() : "application/octet-stream";

        File metadata = new File().setName(file.getOriginalFilename());
        if (StringUtils.hasText(uploadFolderId)) {
            metadata.setParents(Collections.singletonList(uploadFolderId));
        }

        try (InputStream in = file.getInputStream()) {
            InputStreamContent media = new InputStreamContent(contentType, in);
            media.setLength(file.getSize());

            File uploaded = drive.files()
                    .create(metadata, media)
                    .setFields("id")
                    .execute();

            String id = uploaded.getId();

            Permission anyoneReader = new Permission().setType("anyone").setRole("reader");
            drive.permissions().create(id, anyoneReader).setFields("id").execute();

            return id;
        }
    }
}
