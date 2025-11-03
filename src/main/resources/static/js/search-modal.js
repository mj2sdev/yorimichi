(() => {
  // ====== 설정 ======
  const MIN_LEN = 2;                 // 최소 입력 글자수
  const API_URL = "/api/search/suggest";
  const MOCK = true;                 // 서버 연동 후 false 로 변경

  // ====== 목데이터 (MOCK 전용) ======
  const MOCK_DATA = [
    { id: 4001, name: "부산 이자카야", address: "부산 수영구 남천동", category: "IZAKAYA" },
    { id: 2000, name: "라멘 하코다테", address: "서울 마포구 합정동", category: "RAMEN" },
    { id: 1203, name: "스시 마노", address: "대전 서구 둔산동", category: "SUSHI" },
    { id: 9012, name: "부산 라멘", address: "부산 해운대구 좌동", category: "RAMEN" },
    { id: 3456, name: "부산 스시", address: "부산 수영구 남천동", category: "SUSHI" },
    // 샘플 STORE
    { id: 2001, name: "상점01", address: "시도/시군구/도로 01", category: "STORE" },
    { id: 2002, name: "상점02", address: "시도/시군구/도로 02", category: "STORE" },
    { id: 2003, name: "상점03", address: "시도/시군구/도로 03", category: "STORE" },
    { id: 2004, name: "상점04", address: "시도/시군구/도로 04", category: "STORE" },
    { id: 2005, name: "상점05", address: "시도/시군구/도로 05", category: "STORE" },
  ];

  // ====== 유틸 ======
  const debounce = (fn, ms = 250) => { let t; return (...a) => { clearTimeout(t); t = setTimeout(() => fn(...a), ms); }; };
  const escapeHtml = (s = "") => s.replace(/[&<>"']/g, c => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
  const norm = s => (s ?? "").toString().normalize("NFC").toLowerCase().replace(/\s+/g, "");
  const matches = (item, q) => {
    const nq = norm(q);
    return [item.name, item.address, item.category].some(v => norm(v).includes(nq));
  };
  const escapeRegExp = s => s.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
  const highlightMatch = (text = "", q = "") => {
    const esc = escapeHtml(text);
    const tokens = (q || "").toString().trim().split(/\s+/).filter(Boolean).map(escapeRegExp);
    if (tokens.length === 0) return esc;
    const re = new RegExp("(" + tokens.join("|") + ")", "gi");
    return esc.replace(re, '<span class="ym-hl">$1</span>');
  };

  // ====== 빈 결과 안내 토글 ======
  function toggleEmptyBox(emptyBox, show, msg = "검색 결과가 없습니다") {
    if (!emptyBox) return;
    emptyBox.textContent = msg;
    if (show) {
      emptyBox.classList.remove("d-none");
      emptyBox.style.display = "block";
    } else {
      emptyBox.classList.add("d-none");
      emptyBox.style.display = "none";
    }
  }

  // ====== 제안 요청 ======
  async function fetchSuggest(q) {
    if (!q || q.trim().length < MIN_LEN) return [];

    if (MOCK) {
      const list = MOCK_DATA.filter(it => matches(it, q)).slice(0, 10);
      if (list.length > 0) return list;

      // 매칭 0건이면 q 기반으로 즉석 생성
      return Array.from({ length: 5 }, (_, i) => {
        const n = String(i + 1).padStart(2, "0");
        return { id: 10000 + i, name: `${q}${n}`, address: `시도/시군구/도로 ${n}`, category: "STORE" };
      });
    }

    try {
      const url = `${API_URL}?keyword=${encodeURIComponent(q)}&limit=10`;
      const res = await fetch(url, { headers: { "Accept": "application/json" } });
      if (!res.ok) throw new Error("HTTP " + res.status);
      const data = await res.json();
      return (Array.isArray(data) ? data : []).slice(0, 10);
    } catch {
      return [];
    }
  }

  // ====== 공통 렌더러 (스타일 유지 버전) ======
  function renderList({ listBox, emptyBox, q }) {
    return (items) => {
      listBox.innerHTML = "";

      if (!items || items.length === 0) {
        toggleEmptyBox(emptyBox, true);
        return;
      }
      toggleEmptyBox(emptyBox, false);

      const seen = new Set();
      items
        .filter(it => it && !seen.has(it.id) && (seen.add(it.id), true))
        .forEach(item => {
          const li = document.createElement("li");
          const a  = document.createElement("a");
          a.className = "list-group-item list-group-item-action d-flex align-items-center justify-content-between";
          a.href = `/store/detail?id=${encodeURIComponent(item.id)}`;

          const cat = (item.category || "").toString().toUpperCase();
          const catLabel = (cat === "REGION" || cat === "STORE") ? cat : (cat || "STORE");

          a.innerHTML = `
            <div class="d-flex align-items-center gap-3">
              <span class="badge rounded-pill bg-light border text-muted fw-semibold px-3 py-2">${catLabel}</span>
              <div>
                <div class="fw-semibold">${highlightMatch(item.name || "", q)}</div>
                <div class="text-muted small">${highlightMatch(item.address || "", q)}</div>
              </div>
            </div>
            <i class="bi bi-chevron-right text-muted"></i>
          `;

          li.appendChild(a);
          listBox.appendChild(li);
        });
    };
  }

  // ====== 초기화 ======
  document.addEventListener('DOMContentLoaded', () => {
    // ---- (1) 모달 모드: 있으면 우선 사용
    const modalEl    = document.getElementById('searchModal');
    const modalInput = document.getElementById('modalSearchInput');
    const modalList  = document.getElementById('searchSuggest');
    const modalEmpty = document.getElementById('searchEmpty');

    if (modalEl && modalInput && modalList) {
      const headerInput = document.getElementById('headerSearchInput') || document.getElementById('search-input');
      const btnOpen     = document.getElementById('button-search');
      const modal       = new bootstrap.Modal(modalEl, { backdrop: true });

      modalList.classList.add("list-group", "list-group-flush");

      const paint = (q) => renderList({ listBox: modalList, emptyBox: modalEmpty, q });
      const onType = debounce(async () => {
        const q = modalInput.value || "";
        const items = (q.trim().length < MIN_LEN) ? [] : await fetchSuggest(q);
        paint(q)(items);
      }, 200);

      const open = () => {
        modal.show();
        if ((modalInput.value || "").trim().length >= MIN_LEN) onType();
      };

      headerInput && headerInput.addEventListener('focus', open);
      headerInput && headerInput.addEventListener('click', open);
      btnOpen     && btnOpen.addEventListener('click', open);

      modalInput.addEventListener("input", onType);

      if ((modalInput.value || "").trim().length >= MIN_LEN) onType();

      return; // 모달 모드면 여기서 끝
    }

    // ---- (2) 오버레이(헤더 인라인) 모드
    const qInput   = document.getElementById("search-input");
    const panel    = document.getElementById("search-panel");
    const listBox  = document.getElementById("search-list");
    const emptyBox = document.querySelector("#search-panel .search-empty");

    if (!qInput || !panel || !listBox) return;

    listBox.classList.add("list-group", "list-group-flush");

    const openPanel  = () => panel.classList.remove('d-none');
    const closePanel = () => panel.classList.add('d-none');

    const paint = (q) => renderList({ listBox, emptyBox, q });
    const onType = debounce(async () => {
      const q = qInput.value || "";
      if (q.trim().length < MIN_LEN) {
        listBox.innerHTML = "";
        toggleEmptyBox(emptyBox, !!q.trim().length, "검색어를 입력해 주세요");
        openPanel();
        return;
      }
      const items = await fetchSuggest(q);
      paint(q)(items);
      openPanel();
    }, 200);

    qInput.addEventListener("input", onType);
    qInput.addEventListener("focus", () => {
      openPanel();
      if ((qInput.value || "").trim().length >= MIN_LEN) onType();
    });

    // 결과 페이지처럼 값이 이미 있을 때도 바로 표시
    if ((qInput.value || "").trim().length >= MIN_LEN) {
      openPanel();
      onType();
    }

    // 바깥 클릭 시 닫기
    document.addEventListener("click", (e) => {
      if (!panel.contains(e.target) && e.target !== qInput) closePanel();
    });

    // ESC 닫기
    qInput.addEventListener("keydown", (e) => {
      if (e.key === "Escape") closePanel();
    });
  });
})();
