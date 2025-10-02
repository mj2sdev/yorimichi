// src/main/java/com/jslhrd/yorimichi/manager/DriveOAuthUploader.java
package com.jslhrd.yorimichi.manager;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// @Service
@RequiredArgsConstructor
public class DriveOAuthUploader {
    private final Drive drive; // DriveOAuthConfig 에서 만든 OAuth Drive 빈 주입

    public File uploadToMyDrive(MultipartFile mf) throws IOException {
        String folderId = ensureFolder("yorimichi-uploads"); // 폴더 없으면 생성

        File meta = new File()
                .setName(mf.getOriginalFilename())
                .setParents(List.of(folderId));

        InputStreamContent media = new InputStreamContent(
                mf.getContentType() != null ? mf.getContentType() : "application/octet-stream",
                mf.getInputStream()
        );
        media.setLength(mf.getSize());

        return drive.files().create(meta, media)
                .setFields("id,name,parents,webViewLink,webContentLink")
                .execute();
    }

    private String ensureFolder(String name) throws IOException {
        String q = "mimeType='application/vnd.google-apps.folder' and name='" +
                name.replace("'", "\\'") + "' and trashed=false and 'me' in owners";
        FileList list = drive.files().list()
                .setQ(q)
                .setPageSize(1)
                .setFields("files(id,name)")
                .execute();
        if (!list.getFiles().isEmpty()) return list.getFiles().get(0).getId();

        File folder = new File()
                .setName(name)
                .setMimeType("application/vnd.google-apps.folder");
        return drive.files().create(folder)
                .setFields("id")
                .execute()
                .getId();
    }

    // 공개 링크가 필요하면 호출 (선택)
    public void makeAnyoneReadable(String fileId) throws IOException {
        var perm = new com.google.api.services.drive.model.Permission()
                .setType("anyone").setRole("reader");
        drive.permissions().create(fileId, perm).setFields("id").execute();
    }
}
