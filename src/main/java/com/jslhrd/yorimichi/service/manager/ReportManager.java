package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.ReportDTO;
import com.jslhrd.yorimichi.exception.DuplicateReportException;
import com.jslhrd.yorimichi.exception.ReportNotFoundException;
import com.jslhrd.yorimichi.exception.RootNotFoundException;
import com.jslhrd.yorimichi.mapper.ReportMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReportManager implements ReportService {

	private final RootMapper rootMapper;
	private final ReportMapper reportMapper;

	@Override
	@Transactional(readOnly = true)
	public List<ReportDTO> findAll() {
		return reportMapper.selectAll();
	}

	@Override
	public void save(Long userId, Long rootId, ReportDTO report) {

		boolean exists = rootMapper.existsActive(rootId);
		if (!exists) {
			throw new RootNotFoundException(rootId);
		}

		report.setReporterId(userId);
		report.setRootId(rootId);

		try {
			int affected = reportMapper.insert(report);
			if (affected == 0) {
				log.warn("Report insert failed: affected={}, report={}", affected, report);
				throw new IllegalStateException("Report insert failed");
			}
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateReportException(userId, rootId);
		}

		log.info("Report created reportId={}", report.getId());
	}

	@Override
	public void updateStatus(Long reportId, ReportDTO report) {

		int affected = reportMapper.updateStatus(reportId, report);
		if (affected == 1) {
			log.info("Report status updated reportId={}, to={}", reportId, report.getStatus());
			return;
		}

		boolean exists = reportMapper.existsById(reportId);
		if (!exists) {
			throw new ReportNotFoundException(reportId);
		}

		throw new AccessDeniedException("신고 상태를 변경 할 수 없습니다.");
	}

	@Override
	public void cancel(Long userId, Long reportId) {

		int affected = reportMapper.cancelByReporter(userId, reportId);
		if (affected == 1) {
			log.info("Report cancelled reportId={}", reportId);
			return;
		}

		boolean exists = reportMapper.existsById(reportId);
		if (!exists) {
			throw new ReportNotFoundException(reportId);
		}

		throw new AccessDeniedException("신고 취소 권한이 없습니다.");
	}
}