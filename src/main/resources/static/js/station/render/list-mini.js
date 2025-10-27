// /js/station/render/list-mini.js
import {clip, esc, hrefOf} from '../core/dom.js';
import {timeAgo} from '../core/time.js';

export function renderMiniItem(item) {
    const isReview = item?.type === 'REVIEW';
    const a = document.createElement('a');
    a.className = 'list-group-item list-group-item-action d-flex gap-2 align-items-start';
    a.setAttribute('list-id', item?.id);
    a.dataset.type = item?.type ?? '';
    a.href = hrefOf(item);

    // 본문/제목
    const content = isReview ? (item?.review?.content ?? '') : (item?.coeat?.title ?? '');

    // 리뷰: 음식 태그 (FoodDTO.name만 사용, 최대 3개 + 나머지 개수)
    let foodTagsHTML = '';
    if (isReview && Array.isArray(item?.review?.foods) && item.review.foods.length > 0) {
        const names = item.review.foods.map(f => f?.name).filter(Boolean);
        const head = names.slice(0, 3);
        const rest = Math.max(0, names.length - head.length);
        const headHTML = head.map(n => `<span class="badge bg-light text-dark me-1">${esc(n)}</span>`).join('');
        const restHTML = rest > 0 ? `<span class="text-muted">+${rest}</span>` : '';
        foodTagsHTML = `<div class="small">${headHTML}${restHTML}</div>`;
    }

    // 하단 메타: 가게명 / 리뷰 평점 또는 코잇 정원/승인/신청
    const storeName =
        (item?.store?.name)
        || (item?.store?.id != null ? `store #${item.store.id}`
            : (item?.coeat?.storeId != null ? `store #${item.coeat.storeId}` : ''));

    const metaBits = [];
    if (storeName) metaBits.push(esc(storeName));

    if (isReview) {
        if (item?.review?.rating != null) {
            metaBits.push(`★ ${esc(String(item.review.rating))}`);
        }
    } else {
        const cap = item?.coeat?.capacity;
        const approved = item?.coeat?.approvedCount;
        const applied = item?.coeat?.appliedCount;
        const parts = [];
        if (cap != null) parts.push(`정원 ${esc(String(cap))}`);
        if (approved != null) parts.push(`승인 ${esc(String(approved))}`);
        if (applied != null) parts.push(`신청 ${esc(String(applied))}`);
        if (parts.length) metaBits.push(parts.join(' / '));
    }

    a.innerHTML = `
    <i class="bi ${isReview ? 'bi-chat-dots' : 'bi-people'} mt-1"></i>
    <div class="flex-grow-1">
      <div class="d-flex justify-content-between">
        <div class="fw-semibold">${isReview ? '리뷰' : '같이먹기'}</div>
        <div class="text-muted small rel-time" data-iso="${esc(item?.createdAt ?? '')}">
          ${timeAgo(item?.createdAt)}
        </div>
      </div>

      <div class="text-ymsecondary small">${esc(clip(content ?? ''))}</div>

      ${foodTagsHTML}

      <div class="small text-muted">
        ${metaBits.filter(Boolean).join(' · ')}
      </div>
    </div>
  `;

    return a;
}