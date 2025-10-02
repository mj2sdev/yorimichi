package com.jslhrd.yorimichi.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 구글드라이브 API 를 이용해 파일관리를 구현합니다.
 * 
 * @author mj2sdev
 * @since 1.0
 * 
 */
public interface GoogleDriveService {

    /**
     * 단일 파일 업로드(기존). 내부적으로 다중 업로드를 재사용합니다.
     * @return 저장된 이미지의 공개 컨텐츠 URL
     */
    default String uploadFile(MultipartFile file) {
        return uploadFiles(Collections.singletonList(file)).get(0);
    }

    /**
     * 여러 파일을 한 묶음으로 업로드하고 각 파일의 공개 컨텐츠 URL 리스트를 반환합니다.
     */
    List<String> uploadFiles(List<MultipartFile> files);

    /**
     * 편의 varargs 버전.
     */
    default List<String> uploadFiles(MultipartFile... files) {
        return uploadFiles(Arrays.asList(files));
    }
}
