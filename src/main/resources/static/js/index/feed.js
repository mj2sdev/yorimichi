/* ============================================================================
 * Mini Station (index 오른쪽): 최신 리뷰/같이먹기 몇 개만 보여주는 위젯
 * - 기대 응답: [{ type: 'REVIEW'|'COEAT', createdAt: ISO, data: { ...DTO } }, ...]
 * - API가 비어 있으면 "아직 피드가 없어요." 출력
 * - 실패 시 에러 메시지 출력
 * ============================================================================ */
(function () {
  const BOX_SELECTOR = '#mini-station';
  const FEED_COUNT = 6;         // 인덱스에 노출할 개수
  const ENDPOINT = `/feed/${FEED_COUNT}`;

  document.addEventListener('DOMContentLoaded', init);

  async function init() {
    const box = document.querySelector(BOX_SELECTOR);
    if (!box) return;

    // 초기 스켈레톤
    box.innerHTML = skeletonList(3);

    try {
      const res = await fetch(ENDPOINT, { headers: { 'Accept': 'application/json' } });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const items = await res.json();

      if (!Array.isArray(items) || items.length === 0) {
        box.innerHTML = `<div class="list-group-item text-muted small">아직 피드가 없어요.</div>`;
        return;
      }
      box.innerHTML = items.map(renderItem).join('');
    } catch (e) {
      console.error('[mini-station] fetch error:', e);
      box.innerHTML = `<div class="list-group-item text-danger small">피드를 불러오지 못했습니다.</div>`;
    }
  }

  function renderItem(it) {
    const type = it?.type || '';
    const data = it?.data || {};
    const icon = type === 'REVIEW' ? 'bi-chat-dots' : 'bi-people';
    const link = type === 'REVIEW'
      ? `/store/detail?id=${data.storeId}#review-${data.id}`
      : `/coeat/${data.id}`;

    const when = timeAgo(it?.createdAt);
    const body = type === 'REVIEW'
      ? (data.content ?? '')
      : (data.title ?? data.content ?? '');

    const rating = type === 'REVIEW' && data.rating ? ` · ★ ${data.rating}` : '';

    return `
      <a href="${link}" class="list-group-item list-group-item-action d-flex gap-2 align-items-start">
        <i class="bi ${icon} mt-1"></i>
        <div class="flex-grow-1">
          <div class="d-flex justify-content-between">
            <div class="fw-semibold">${type === 'REVIEW' ? '리뷰' : '같이먹기'}</div>
            <div class="text-muted small">${when}</div>
          </div>
          <div class="text-ymsecondary small">${escapeHtml((body || '').replace(/\s+/g, ' ').slice(0, 60))}</div>
          <div class="small text-muted">store #${data.storeId ?? '-'}${rating}</div>
        </div>
      </a>`;
  }

  // -------- utils --------
  function timeAgo(iso) {
    if (!iso) return '';
    try {
      const t = new Date(iso);
      const diff = (Date.now() - t.getTime()) / 1000;
      if (diff < 60) return '방금';
      if (diff < 3600) return Math.floor(diff / 60) + '분 전';
      if (diff < 86400) return Math.floor(diff / 3600) + '시간 전';
      return Math.floor(diff / 86400) + '일 전';
    } catch { return ''; }
  }
  function escapeHtml(s) {
    return String(s).replace(/[&<>"']/g, m => ({
      '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
    })[m]);
  }
  function skeletonList(n) {
    return Array.from({ length: n }).map(() => `
      <div class="list-group-item">
        <div class="d-flex gap-2 align-items-start">
          <div class="skeleton" style="width:16px;height:16px;border-radius:4px;"></div>
          <div class="flex-grow-1">
            <div class="skeleton" style="height:12px;border-radius:6px;"></div>
            <div class="skeleton mt-2" style="height:10px;width:70%;border-radius:5px;"></div>
          </div>
        </div>
      </div>`).join('');
  }
})();
