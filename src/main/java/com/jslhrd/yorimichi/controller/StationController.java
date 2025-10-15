package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.domain.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 정류장(피드) 컨트롤러
 *
 * - /station : 정류장 페이지 이동 (뷰 렌더)
 * - /feed, /feed/{count} : 인덱스 우측 미니 정류장 위젯용 API (더미 데이터 반환)
 *
 * 실제 서비스 연동 전, 화면/디자인 검증 목적으로 더미 데이터를 내려줍니다.
 * 나중에 서비스/매퍼 연동 시 makeDummy() 대신 서비스 호출로 교체하면 됩니다.
 */
@Controller
@RequiredArgsConstructor
public class StationController {

    // 실제 연동 시 사용 예정
    // private final StationService stationService;

    /** 정류장 페이지 */
    @GetMapping("/station")
    public String showStation(Model model) {
        // TODO: 서비스 연동 전이라 별도 모델 주입 없이 뷰만 이동
        // (원하면 아래 makeDummy(20)으로 SSR 목록을 내려도 됨)
        return "station/list";
    }

    /** 기본 개수(6개) 피드 */
    @ResponseBody
    @GetMapping({"/feed", "/feed/"})
    public List<Map<String, Object>> showFeedDefault() {
        return makeDummy(6);
    }

    /** 요청한 개수만큼 피드 */
    @ResponseBody
    @GetMapping("/feed/{count}")
    public List<Map<String, Object>> showFeed(@PathVariable("count") int count) {
        return makeDummy(count);
    }

    // ----------------------------------------------------------------------
    // 더미 생성 로직 (REVIEW / COEAT 교차 생성)
    // createdAt은 ISO 문자열로 내려 JS Date 파싱이 잘 되도록 함.
    // ----------------------------------------------------------------------
    private List<Map<String, Object>> makeDummy(int count) {
        int n = Math.max(1, count);
        LocalDateTime now = LocalDateTime.now();

        List<Map<String, Object>> out = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                // REVIEW
                ReviewDTO r = new ReviewDTO();
                r.setId((long) (i + 1));
                r.setUserId(1000L + i);
                r.setStoreId(2000L + i);
                r.setRating(4 + (i % 2)); // 4~5
                r.setContent("분위기와 맛 모두 훌륭했어요! (" + (i + 1) + ")");

                Map<String, Object> item = new HashMap<>();
                item.put("type", "REVIEW");
                item.put("createdAt", now.minusMinutes(i * 5L).toString()); // 최신순
                item.put("data", r);
                out.add(item);

            } else {
                // COEAT
                CoeatDTO c = new CoeatDTO();
                c.setId((long) (i + 1));
                c.setUserId(3000L + i);
                c.setStoreId(4000L + i);
                c.setTitle("같이 먹을 분 구해요 (" + (i + 1) + ")");
                c.setContent("오늘 저녁 7시에 가실 분? 소수 정예로 가요!");
                c.setMeetingAt(now.plusDays(1).withHour(19).withMinute(0).withSecond(0));

                Map<String, Object> item = new HashMap<>();
                item.put("type", "COEAT");
                item.put("createdAt", now.minusMinutes(i * 5L + 2).toString());
                item.put("data", c);
                out.add(item);
            }
        }

        // createdAt 기준 최신순 정렬
        out.sort((a, b) -> {
            LocalDateTime t1 = LocalDateTime.parse((String) a.get("createdAt"));
            LocalDateTime t2 = LocalDateTime.parse((String) b.get("createdAt"));
            return t2.compareTo(t1);
        });

        // 요청 개수만큼 제한
        if (out.size() > count) {
            return out.subList(0, count);
        }
        return out;
    }
}
