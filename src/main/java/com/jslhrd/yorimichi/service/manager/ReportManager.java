package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.ReportDTO;
import com.jslhrd.yorimichi.enums.ReportStatus;
import com.jslhrd.yorimichi.exception.*;
import com.jslhrd.yorimichi.mapper.ReportMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jslhrd.yorimichi.enums.ReportStatus.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReportManager implements ReportService {

	private final UserMapper userMapper;
	private final RootMapper rootMapper;
	private final ReportMapper reportMapper;

	@Override
	@Transactional(readOnly = true)
	public List<ReportDTO> findAll() {
		return reportMapper.selectAll();
	}

	@Override
	public void save(Long userId, Long rootId, ReportDTO report) {

		assertActiveUser(userId);
		assertActiveRoot(rootId);

		report.setReporterId(userId);
		report.setRootId(rootId);

		try {
			reportMapper.insert(report);
		} catch (DuplicateKeyException e) {
			throw new DuplicateReportException(userId, rootId);
		}

		log.info("Report: created reportId={}", report.getId());
	}

	@Override
	public void updateStatus(Long reportId, ReportDTO report) {

		ReportStatus to = report.getStatus();
		ReportStatus cur = reportMapper.selectStatus(reportId)
				.orElseThrow(() -> new ReportNotFoundException(reportId));

		if (!(cur == PENDING && (to == RESOLVED || to == REJECTED))) {
			throw new BadRequestException("허용되지 않은 전이");
		}

		boolean affected = reportMapper.updateStatus(reportId, cur, to) > 0;
		if (!affected) {
			ReportStatus after = reportMapper.selectStatus(reportId)
					.orElseThrow(() -> new ReportNotFoundException(reportId));
			if (after == to) {
				throw new BadRequestException("이미 처리되었습니다.");
			}
			if (after == PENDING) {
				throw new ConflictException("정원 초과 또는 동시 승인 충돌");
			}
			throw new BadRequestException("허용되지 않은 전이");
		}

		log.info("Report: status updated reportId={}, report={}", reportId, report);
	}

	@Override
	public void cancel(Long userId, Long reportId) {

		ReportStatus cur = reportMapper.selectStatus(reportId)
				.orElseThrow(() -> new ReportNotFoundException(reportId));

		if (cur == RESOLVED || cur == REJECTED) {
			throw new BadRequestException("해결/거절된 신고는 취소할 수 없습니다.");
		}

		boolean affected = reportMapper.cancelByReporter(userId, reportId) > 0;
		if (!affected) {
			log.debug("Report: cancel no-op userId={}, reportId={}", userId, reportId);
			return;
		}

		log.info("Report: cancelled userId={} reportId={}", userId, reportId);
	}

	private void assertActiveRoot(Long rootId) {
		boolean exists = rootMapper.existsActive(rootId);
		if (!exists) {
			throw new RootNotFoundException(rootId);
		}
	}

	private void assertActiveUser(Long userId) {
		boolean exists = userMapper.existsActive(userId);
		if (!exists) {
			throw new UserNotFoundException(userId);
		}
	}
}