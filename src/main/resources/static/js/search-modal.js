(() => {
  // ====== 설정 ======
  const MIN_LEN = 2;                    // 최소 입력 글자수
  const API_URL = "/api/search/suggest";
  const MOCK    = false;                // 실제 API 사용 시 false

  // ====== (개발용) 목데이터 ======
  const MOCK_DATA = [
    { id: 2001, name: "상점01", address: "시도/시군구/도로 01", category: "STORE" },
    { id: 2002, name: "상점02", address: "시도/시군구/도로 02", category: "STORE" },
    { id: 2003, name: "상점03", address: "시도/시군구/도로 03", category: "STORE" },
    { id: 2004, name: "상점04", address: "시도/시군구/도로 04", category: "STORE" },
    { id: 2005, name: "상점05", address: "시도/시군구/도로 05", category: "STORE" },
  ];

  // ====== 유틸 ======
  const debounce = (fn, ms = 250) => { let t; return (...a) => { clearTimeout(t); t = setTimeout(() => fn(...a), ms); }; };
  const escapeHtml = (s = "") => s.replace(/[&<>"']/g, c => ({ "&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;" }[c]));
  const escapeRegExp = s => s.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
  const highlightMatch = (text = "", q = "") => {
    const esc = escapeHtml(text);
    const tokens = (q || "").toString().trim().split(/\s+/).filter(Boolean).map(escapeRegExp);
    if (!tokens.length) return esc;
    return esc.replace(new RegExp("(" + tokens.join("|") + ")", "gi"), '<span class="ym-hl">$1</span>');
  };
  const toggleEmptyBox = (emptyBox, show, msg = "検索結果がありません") => {
    if (!emptyBox) return;
    emptyBox.textContent = msg;
    emptyBox.classList.toggle("d-none", !show);
    emptyBox.style.display = show ? "block" : "none";
  };

  // ====== 서버 응답 -> 표준 포맷 매핑 ======
  function mapServerItem(it = {}) {
    const id =
      it.id ?? it.storeId ?? it.store_id ??
      it.regionId ?? it.region_id ??
      it.addressId ?? it.address_id ??
      it.itemId ?? it.item_id ?? null;

    const name =
      it.name ?? it.storeName ?? it.store_name ??
      it.title ?? it.regionName ?? it.region_name ??
      it.keyword ?? it.label ?? it.text ?? "";

    const addrObj = it.address || it.addr || it.addressDto || {};
    const address =
      addrObj.roadAddressText ?? addrObj.road_address_text ??
      addrObj.text ?? addrObj.full ?? addrObj.fullAddress ??
      it.roadAddressText ?? it.road_address_text ??
      it.addressText ?? it.address_text ??
      it.fullAddress ?? it.full_address ??
      it.address ?? it.addr ?? "";

    const rawType = (it.type ?? it.category ?? (it.regionId || it.region_id ? "REGION" : "STORE") ?? "")
                    .toString().toUpperCase();
    const category = rawType === "REGION" || rawType === "STORE" ? rawType : "STORE";

    return { id, name, address, category };
  }

  // ====== 제안 요청 ======
  async function fetchSuggest(q) {
    if (!q || q.trim().length < MIN_LEN) return [];

    if (MOCK) {
      return MOCK_DATA.filter(it =>
        [it.name, it.address, it.category].some(v => (v || "").toLowerCase().includes(q.toLowerCase()))
      ).slice(0, 10);
    }

    try {
      const res = await fetch(`${API_URL}?q=${encodeURIComponent(q)}&limit=10`, {
        headers: { "Accept": "application/json" }
      });
      if (!res.ok) throw new Error("HTTP " + res.status);
      const data = await res.json();

      const raw = Array.isArray(data)       ? data
               : Array.isArray(data.items)  ? data.items
               : Array.isArray(data.data)   ? data.data
               : Array.isArray(data.list)   ? data.list
               : Array.isArray(data.result) ? data.result
               : Array.isArray(data.results)? data.results
               : Array.isArray(data.content)? data.content
               : Array.isArray(data.records)? data.records
               : Array.isArray(data.suggestions) ? data.suggestions
               : [];

      return raw.map(mapServerItem).filter(it => it.id).slice(0, 10);
    } catch {
      return [];
    }
  }

  // ====== 렌더러 (원래 스타일 유지) ======
  function renderList({ listBox, emptyBox, q }) {
    return (items) => {
      listBox.innerHTML = "";
      if (!items || items.length === 0) { toggleEmptyBox(emptyBox, true); return; }
      toggleEmptyBox(emptyBox, false);

      const seen = new Set();
      items.filter(it => it && !seen.has(it.id) && (seen.add(it.id), true)).forEach(item => {
        const li = document.createElement("li");
        const a  = document.createElement("a");
        a.className = "list-group-item list-group-item-action d-flex align-items-center justify-content-between";

        const isRegion = (item.category || "").toUpperCase() === "REGION";
        a.href = isRegion
          ? `/search?type=region&id=${encodeURIComponent(item.id)}&q=${encodeURIComponent(item.name ?? "")}`
          : `/store/detail/${encodeURIComponent(item.id)}`;

        const catLabel = isRegion ? "REGION" : "STORE";
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

  // ====== 부트스트랩: 모달/오버레이 모두 지원 ======
  document.addEventListener('DOMContentLoaded', () => {
    // (1) 모달 모드
    const modalEl    = document.getElementById('searchModal');
    const modalInput = document.getElementById('modalSearchInput');
    const modalList  = document.getElementById('searchSuggest');
    const modalEmpty = document.getElementById('searchEmpty');

    if (modalEl && modalInput && modalList) {
      const headerInput = document.getElementById('headerSearchInput') || document.getElementById('search-input');
      const btnOpen     = document.getElementById('button-search');
      const modal       = new bootstrap.Modal(modalEl, { backdrop: true });

      modalList.classList.add("list-group","list-group-flush");

      const paint = (q) => renderList({ listBox: modalList, emptyBox: modalEmpty, q });
      const onType = debounce(async () => {
        const q = modalInput.value || "";
        const items = (q.trim().length < MIN_LEN) ? [] : await fetchSuggest(q);
        paint(q)(items);
      }, 200);

      const open = () => { modal.show(); if ((modalInput.value || "").trim().length >= MIN_LEN) onType(); };
      headerInput && headerInput.addEventListener('focus', open);
      headerInput && headerInput.addEventListener('click', open);
      btnOpen     && btnOpen.addEventListener('click', open);
      modalInput.addEventListener("input", onType);

      if ((modalInput.value || "").trim().length >= MIN_LEN) onType();
      return;
    }

    // (2) 오버레이(헤더 인라인) 모드
    const qInput   = document.getElementById("search-input");
    const panel    = document.getElementById("search-panel");
    const listBox  = document.getElementById("search-list");
    const emptyBox = document.querySelector("#search-panel .search-empty");
    if (!qInput || !panel || !listBox) return;

    listBox.classList.add("list-group","list-group-flush");
    const openPanel  = () => panel.classList.remove('d-none');
    const closePanel = () => panel.classList.add('d-none');

    const paint = (q) => renderList({ listBox, emptyBox, q });
    const onType = debounce(async () => {
      const q = qInput.value || "";
      if (q.trim().length < MIN_LEN) {
        listBox.innerHTML = ""; toggleEmptyBox(emptyBox, !!q.trim().length, "검색어를 입력해 주세요");
        openPanel(); return;
      }
      const items = await fetchSuggest(q);
      paint(q)(items);
      openPanel();
    }, 200);

    qInput.addEventListener("input", onType);
    qInput.addEventListener("focus", () => { openPanel(); if ((qInput.value || "").trim().length >= MIN_LEN) onType(); });

    // 페이지 진입 시 값이 이미 있으면 즉시 조회(검색결과 페이지 대응)
    if ((qInput.value || "").trim().length >= MIN_LEN) { openPanel(); onType(); }

    // 바깥 클릭/ESC 닫기
    document.addEventListener("click", (e) => { if (!panel.contains(e.target) && e.target !== qInput) closePanel(); });
    qInput.addEventListener("keydown", (e) => { if (e.key === "Escape") closePanel(); });
  });
})();
