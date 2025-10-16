package com.jslhrd.yorimichi.service.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.jslhrd.yorimichi.service.GoogleDriveService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDriveManager implements GoogleDriveService {

	private final Drive drive;

	@Value("${google.drive.image-path-prefix}")
	private String imagePathPrefix;

	@Value("${google.drive.folder-id}")
	private String folderId;

	@Override
	public List<String> uploadFiles(List<MultipartFile> files) {
		List<String> urls = new ArrayList<>();
		files.stream()
			.forEach(file -> {
				try {
					File fileMetadata = new File();
					fileMetadata.setName(file.getOriginalFilename());
					fileMetadata.setParents(Collections.singletonList(folderId));

					java.io.File tempFile = java.io.File.createTempFile("upload-", file.getOriginalFilename());
					file.transferTo(tempFile);
					FileContent fileContent = new FileContent(file.getContentType(), tempFile);
					File uploadedFile = drive.files()
						.create(fileMetadata, fileContent)
						.setFields("id, name, webViewLink")
						.execute();
					urls.add(imagePathPrefix + uploadedFile.getId());

				} catch (Exception e) {
					log.error("google drice upload error: {}", e.getLocalizedMessage());
				}
			});

		return urls;
	}
	
}
