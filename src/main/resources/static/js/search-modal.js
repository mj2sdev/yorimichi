(() => {
  // ====== 설정 ======
  const MIN_LEN = 2;                // 최소 입력 글자수
  const API_URL = "/api/search/suggest";
  const MOCK = true;               // UI 테스트용 목데이터 사용 여부 (필요시 true로)

  // 목데이터 (MOCK=true일 때만 사용)
  const MOCK_DATA = [
    { id: 4001, name: "부산 이자카야", address: "부산 수영구 남천동", category: "이자카야" },
    { id: 2000, name: "라멘 하코다테", address: "서울 마포구 합정동", category: "라멘" },
    { id: 1203, name: "스시 마노", address: "대전 서구 둔산동", category: "스시" },
    { id: 9012, name: "부산 라멘", address: "부산 해운대구 좌동", category: "라멘" },
    { id: 3456, name: "부산 스시", address: "부산 수영구 남천동", category: "스시" },
  ];

  // ====== 엘리먼트 ======
  const qInput   = document.getElementById("modalSearchInput");
  const listBox  = document.getElementById("searchSuggest");
  const emptyBox = document.getElementById("searchEmpty");
  if (!qInput || !listBox) return;

  // ====== 유틸 ======
  const debounce = (fn, ms = 250) => { let t; return (...a) => { clearTimeout(t); t = setTimeout(() => fn(...a), ms); }; };
  const escapeHtml = (s = "") => s.replace(/[&<>"']/g, c => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
  const norm = s => (s ?? "").toString().normalize("NFC").toLowerCase().replace(/\s+/g, "");
  const matches = (item, q) => {
    const nq = norm(q);
    return [item.name, item.address, item.category].some(v => norm(v).includes(nq));
  };
// 특수문자 이스케이프
const escapeRegExp = s => s.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");

// q의 각 토큰을 주황색으로 감싸기 (대소문자 무시)
function highlightMatch(text = "", q = "") {
  const esc = escapeHtml(text);
  const tokens = (q || "")
    .toString()
    .trim()
    .split(/\s+/)          // 공백 기준 토큰화
    .filter(t => t.length > 0)
    .map(escapeRegExp);

  if (tokens.length === 0) return esc;

  const re = new RegExp("(" + tokens.join("|") + ")", "gi");
  return esc.replace(re, '<span class="ym-hl">$1</span>');
}

  // ====== 데이터 요청 ======
  async function fetchSuggest(q) {
    if (!q || q.trim().length < MIN_LEN) return [];

    // 목 테스트 모드
    if (MOCK) {
      // 서버 없다면 UI만 확인할 때 사용 — 입력값으로 필터
      return MOCK_DATA.filter(it => matches(it, q)).slice(0, 10);
    }

    // 실제 서버 호출
    try {
      const url = `${API_URL}?keyword=${encodeURIComponent(q)}&limit=10`;
      const res = await fetch(url, { headers: { "Accept": "application/json" } });
      if (!res.ok) throw new Error("HTTP " + res.status);
      const data = await res.json();
      // 서버가 충분히 필터하더라도 프론트에서 한 번 더 안전 필터
      return (Array.isArray(data) ? data : []).filter(it => matches(it, q)).slice(0, 10);
    } catch (e) {
      // 서버 미구현/오류면 아무 것도 안 보여줌 (더미 출력 금지)
      return [];
    }
  }

  // ====== 렌더링 ======
  function render(items) {
    listBox.innerHTML = "";
    if (!items || items.length === 0) {
      emptyBox.style.display = "block";
      return;
    }
    emptyBox.style.display = "none";

    const seen = new Set();
    items.filter(it => it && !seen.has(it.id) && (seen.add(it.id), true))
         .forEach(item => {
           const a = document.createElement("a");
           a.className = "list-group-item list-group-item-action d-flex justify-content-between align-items-center";
           a.href = `/store/detail?id=${encodeURIComponent(item.id)}`;
           a.innerHTML = `
             <div>
               <div class="fw-semibold">${highlightMatch(item.name || "", qInput.value)}</div>
               <div class="text-muted small">${highlightMatch(item.address || "", qInput.value)}</div>
             </div>
             <i class="bi bi-chevron-right text-muted"></i>`;
           listBox.appendChild(a);
         });
  }

  // ====== 입력 핸들러 ======
  const onType = debounce(async () => {
    const q = qInput.value;
    if (q.trim().length < MIN_LEN) {
      listBox.innerHTML = "";
      emptyBox.style.display = q.trim().length ? "block" : "none";
      return;
    }
    const items = await fetchSuggest(q);
    render(items);
  }, 200);

  qInput.addEventListener("input", onType);
})();


document.addEventListener('DOMContentLoaded', () => {
  const input = document.getElementById('headerSearchInput');
  const btn   = document.getElementById('button-search');
  const modalEl = document.getElementById('searchModal');
  if (!modalEl) return;
  const modal = new bootstrap.Modal(modalEl, {backdrop:true});
  const open = () => modal.show();
  input && input.addEventListener('focus', open);  // 포커스만 돼도 열림
  input && input.addEventListener('click', open);
  btn   && btn.addEventListener('click', open);
});
