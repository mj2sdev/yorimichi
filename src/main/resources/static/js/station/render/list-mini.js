// /js/station/render/list-mini.js
// Mini Station: index 우측 위젯에서 아이템(리뷰/같이먹기)을 렌더링
// - 안전(XSS)과 레이아웃 안정성에 집중
// - 말줄임은 clip + CSS .clamp-2 조합

import {clip, esc, hrefOf} from '../core/dom.js';
import {timeAgo} from '../core/time.js';

// 목록용 본문/제목 최대 길이(한글 기준 70~100 권장)
const CONTENT_MAX = 30;

/**
 * meetingAt ISO 문자열을 MM/DD HH:mm로 포맷
 * @param {string} iso
 * @returns {string}
 */
const fmtMeet = (iso) => {
    if (!iso) return '';
    try {
        const d = new Date(iso); // '...Z'도 브라우저에서 자동 보정
        const mm = String(d.getMonth() + 1).padStart(2, '0');
        const dd = String(d.getDate()).padStart(2, '0');
        const hh = String(d.getHours()).padStart(2, '0');
        const mi = String(d.getMinutes()).padStart(2, '0');
        return `${mm}/${dd} ${hh}:${mi}`;
    } catch {
        return '';
    }
};

/** 상태 배지 HTML (같이먹기 전용) */
const buildStatusBadge = (status) => {
    if (!status) return '';
    let cls = 'bg-danger bg-opacity-10 text-danger';
    if (status === 'OPEN') cls = 'bg-success bg-opacity-10 text-success';
    else if (status === 'CLOSED') cls = 'bg-secondary bg-opacity-10 text-secondary';
    return `<span class="badge rounded-pill small ms-2 ${cls}">${esc(status)}</span>`;
};

/** 리뷰 음식 태그(최대 3개 + 나머지 개수) */
const buildFoodTags = (foods) => {
    if (!Array.isArray(foods) || foods.length === 0) return '';
    const names = foods.map((f) => f?.name).filter(Boolean);
    if (names.length === 0) return '';
    const head = names.slice(0, 3);
    const rest = Math.max(0, names.length - head.length);
    const headHTML = head
        .map((n) => `<span class="badge bg-light text-dark me-1">${esc(n)}</span>`)
        .join('');
    const restHTML = rest > 0 ? `<span class="text-muted">+${rest}</span>` : '';
    return `<div class="small">${headHTML}${restHTML}</div>`;
};

/** 1줄 메타(가게명 + 리뷰★ or 코잇 정원/승인/신청) */
const buildMetaLine = (item, isReview) => {
    // 가게명 추출: store.name 우선 → id 백업
    const storeName = (item?.store?.name)
        ?? (item?.store?.id != null ? `store #${item.store.id}`
            : (item?.coeat?.storeId != null ? `store #${item.coeat.storeId}` : ''));

    const bits = [];
    if (storeName) bits.push(esc(storeName));

    if (isReview) {
        const rating = item?.review?.rating;
        if (rating != null) bits.push(`<span class="text-warning">★ ${esc(String(rating))}</span>`);
    } else {
        const cap = item?.coeat?.capacity;
        const approved = item?.coeat?.approvedCount;
        const applied = item?.coeat?.appliedCount;
        const parts = [];
        if (cap != null) parts.push(`정원 ${esc(String(cap))}`);
        if (approved != null) parts.push(`승인 ${esc(String(approved))}`);
        if (applied != null) parts.push(`신청 ${esc(String(applied))}`);
        if (parts.length) bits.push(parts.join(' / '));
    }
    return bits.filter(Boolean).join(' · ');
};

/** 2줄 메타(코잇 전용: 조회/댓글) */
const buildMeta2 = (item, isReview) => {
    if (isReview) return '';
    const views = item?.coeat?.viewCount;
    const cmt = item?.coeat?.commentCount;
    const parts = [];
    if (views != null) parts.push(`<i class="bi bi-eye me-1"></i>${esc(String(views))}`);
    if (cmt != null) parts.push(`<i class="bi bi-chat-dots me-1"></i>${esc(String(cmt))}`);
    return parts.length ? `<div class="small text-muted">${parts.join(' · ')}</div>` : '';
};

/**
 * 미니 정류장 아이템 렌더
 * @param {object} item - 서버에서 내려오는 단일 아이템
 * @returns {HTMLAnchorElement}
 */
export function renderMiniItem(item) {
    const isReview = item?.type === 'REVIEW';
    const a = document.createElement('a');
    a.className = 'list-group-item list-group-item-action d-flex gap-2 align-items-start';
    a.setAttribute('list-id', item?.id);
    a.dataset.type = item?.type ?? '';
    a.href = hrefOf(item);

    // 본문/제목(리뷰: content, 코잇: title)
    const contentRaw = isReview ? (item?.review?.content ?? '') : (item?.coeat?.title ?? '');
    const clipped = clip(contentRaw ?? '', CONTENT_MAX, '…');

    // 같이먹기 상태 배지 / 음식 태그 / 메타들
    const statusBadge = !isReview ? buildStatusBadge(item?.coeat?.status ?? null) : '';
    const foodTagsHTML = isReview ? buildFoodTags(item?.review?.foods) : '';
    const meta1 = buildMetaLine(item, isReview);
    const meta2 = buildMeta2(item, isReview);

    // meetingAt (같이먹기 전용)
    const meetingLine = (!isReview && item?.coeat?.meetingAt)
        ? `<div class="small text-muted"><i class="bi bi-calendar-event me-1"></i>${esc(fmtMeet(item.coeat.meetingAt))}</div>`
        : '';

    a.innerHTML = `
    <i class="bi ${isReview ? 'bi-chat-dots' : 'bi-people'} mt-1"></i>
    <div class="flex-grow-1">
      <div class="d-flex justify-content-between">
        <div class="fw-semibold">
          ${isReview ? '리뷰' : '같이먹기'}${statusBadge}
        </div>
        <div class="text-muted small rel-time" data-iso="${esc(item?.createdAt ?? '')}">
          ${timeAgo(item?.createdAt)}
        </div>
      </div>

      <div class="text-ymsecondary small mb-2 lh-base clamp-2" title="${esc(contentRaw)}">
        ${esc(clipped)}
      </div>

      ${meetingLine}
      ${foodTagsHTML}

      <div class="small text-muted">${meta1}</div>
      ${meta2}
    </div>
  `;

    return a;
}

/**
 * (선택) 컨테이너에 아이템 리스트 렌더링
 * @param {HTMLElement} container - ul/list-group 컨테이너
 * @param {Array<object>} items
 */
export function renderMiniList(container, items = []) {
    if (!container) return;
    const frag = document.createDocumentFragment();
    items.forEach((it) => frag.appendChild(renderMiniItem(it)));
    container.appendChild(frag);
}