package com.jslhrd.yorimichi.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 구글드라이브 API 를 이용해 파일관리를 구현합니다.
 * 
 * @author mj2sdev
 * @since 1.0 초안 작성
 * 
 * @author GeonHoKoo
 * @version 1.1 편의 메서드 등 추가
 * 
 * @author mj2sdev
 * @version 1.2 주석 수정
 * 
 */
public interface GoogleDriveService {

    /**
     * 단일 파일 업로드(기존). 내부적으로 다중 업로드를 재사용합니다.
     * @param file 멀티파트 파일
     * @return 저장된 이미지의 공개 컨텐츠 URL
     */
    default String uploadFile(MultipartFile file) {
        return uploadFiles(Collections.singletonList(file)).get(0);
    }

    /**
     * 여러 파일을 한 묶음으로 업로드하고 각 파일의 공개 컨텐츠 URL 리스트를 반환합니다.
     * 
     * @param files 멀티파트 파일 리스트
     * @return {@code List<String>} 파일(이미지) 링크 리스트
     */
    List<String> uploadFiles(List<MultipartFile> files);

    /**
     * multipartFile 편리한 args 버전 powered by GeonHoKoo
     * 
     * @param files 멀티파트 파일 배열, 단일파일 나열 등
     * @return {@code List<String>} 파일 접근가능 링크 리스트
     */
    default List<String> uploadFiles(MultipartFile... files) {
        return uploadFiles(Arrays.asList(files));
    }
}
