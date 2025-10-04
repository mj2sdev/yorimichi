package com.jslhrd.yorimichi.service;


import com.jslhrd.yorimichi.domain.ReportDTO;


/**
 * 신고 관련 서비스 인터페이스
 * 신고와 관련된 기능 관리
 * 
 * @author 3ll3702
 * @since 1.0
 */
public interface ReportService {
    
    /**
     * 
     * @param category 대상의 분류
     * @param rootId 대상의 id.
     * @param userId 신고자 id
     */
    public void saveReport(String category, Long rootId, Long userId, ReportDTO report);

    /**
     * 
     * @param reportId 신고Id를 통해 검색해서 report에 대한 조치.
     * 접수된 신고에 따른 조치가 이루어져야 합니다.
     */
    public void putReport(Long reportId);

    /**
     * 
     * @param reportId 신고Id를 통해 검색해서 report에 대한 반려.
     * 접수된 신고를 거절처리합니다.
     */
    public void deleteReport(Long reportId);
}
