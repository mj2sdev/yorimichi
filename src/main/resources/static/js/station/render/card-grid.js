// /js/station/render/card-grid.js
import {esc, hrefOf} from '../core/dom.js';
import {timeAgo} from '../core/time.js';

const fmtMeet = (iso) => {
    if (!iso) return '';
    try {
        const d = new Date(iso);
        const mm = String(d.getMonth() + 1).padStart(2, '0');
        const dd = String(d.getDate()).padStart(2, '0');
        const hh = String(d.getHours()).padStart(2, '0');
        const mi = String(d.getMinutes()).padStart(2, '0');
        return `${mm}/${dd} ${hh}:${mi}`;
    } catch {
        return '';
    }
};

export function renderCardItem(item) {
    const isReview = item?.type === 'REVIEW';

    const a = document.createElement('a');
    a.className = 'text-decoration-none grid-item';
    a.setAttribute('list-id', item?.id);
    a.dataset.type = item?.type ?? '';
    a.href = hrefOf(item);

    const card = document.createElement('div');
    card.className = 'card shadow-sm border-0 hover-shadow';

    const body = document.createElement('div');
    body.className = 'card-body p-3 d-flex flex-column';

    // 1) 헤더: 타입/배지 + 시간 + (같이먹기 상태)
    const head = document.createElement('div');
    head.className = 'd-flex align-items-start justify-content-between mb-2';

    const left = document.createElement('div');
    left.className = 'd-flex align-items-center gap-2';
    left.innerHTML = `
    <i class="bi fs-5 ${isReview ? 'bi-chat-square-text-fill text-primary' : 'bi-people-fill text-warning'}"></i>
    <span class="badge rounded-pill small ${isReview ? 'bg-primary bg-opacity-10 text-primary' : 'bg-warning bg-opacity-10 text-warning'}">
      ${isReview ? 'レビュー' : '一緒に食べる'}
    </span>`;

    // 상태 배지(같이먹기)
    const status = item?.coeat?.status ?? null;
    if (!isReview && status) {
        let cls = 'bg-danger bg-opacity-10 text-danger';
        if (status === 'OPEN') cls = 'bg-success bg-opacity-10 text-success';
        else if (status === 'CLOSED') cls = 'bg-secondary bg-opacity-10 text-secondary';
        const badge = document.createElement('span');
        badge.className = `badge rounded-pill small ${cls}`;
        badge.textContent = status;
        left.appendChild(badge);
    }

    const time = document.createElement('small');
    time.className = 'text-muted rel-time';
    time.dataset.iso = item?.createdAt ?? '';
    time.textContent = timeAgo(item?.createdAt);

    head.appendChild(left);
    head.appendChild(time);

    // 2) 대표 이미지 (ratio-box)
    const imgUrl = item?.image?.url;
    if (imgUrl) {
        const wrap = document.createElement('div');
        wrap.className = 'ratio-box mb-2';
        const img = document.createElement('img');
        img.src = imgUrl;
        img.alt = '';
        img.loading = 'lazy';
        wrap.appendChild(img);
        body.appendChild(head);
        body.appendChild(wrap);
    } else {
        body.appendChild(head);
    }

    // 3) 사용자명
    const user = document.createElement('div');
    user.className = 'mb-2';
    user.innerHTML = `<span class="fw-semibold text-dark small"><i class="bi bi-person-circle me-1"></i>${esc(item?.user?.nickname ?? '익명')}</span>`;
    body.appendChild(user);

    // 4) 제목/본문
    const preview = document.createElement('div');
    preview.className = 'text-ymsecondary small mb-2 lh-base clamp-2';
    preview.textContent = isReview ? (item?.review?.content ?? '') : (item?.coeat?.title ?? '');
    body.appendChild(preview);

    // 4-0) meetingAt (같이먹기 전용) — 제목/본문 아래
    if (!isReview && item?.coeat?.meetingAt) {
        const meet = document.createElement('div');
        meet.className = 'small text-muted mb-2';
        meet.innerHTML = `<i class="bi bi-calendar-event me-1"></i>${esc(fmtMeet(item.coeat.meetingAt))}`;
        body.appendChild(meet);
    }

    // 4-1) 같이먹기 내용
    if (!isReview) {
        const coContent = item?.coeat?.content ?? '';
        if (coContent) {
            const cc = document.createElement('div');
            cc.className = 'text-muted small mb-2 clamp-2';
            cc.textContent = coContent;
            body.appendChild(cc);
        }
    }

    // 5) 리뷰: 음식 뱃지
    if (isReview && Array.isArray(item?.review?.foods) && item.review.foods.length > 0) {
        const foodsWrap = document.createElement('div');
        foodsWrap.className = 'mb-2';
        const bag = document.createElement('div');
        bag.className = 'd-flex flex-wrap gap-1';
        for (const f of item.review.foods) {
            const name = f?.name;
            if (!name) continue;
            const badge = document.createElement('span');
            badge.className = 'badge bg-light text-dark';
            badge.textContent = name;
            bag.appendChild(badge);
        }
        foodsWrap.appendChild(bag);
        body.appendChild(foodsWrap);
    }

    // 6) 같이먹기: 정원/승인/신청 (상점/주소 위)
    if (!isReview) {
        const cap = item?.coeat?.capacity;
        const approved = item?.coeat?.approvedCount;
        const applied = item?.coeat?.appliedCount;
        const parts = [];
        if (cap != null) parts.push(`정원 ${esc(String(cap))}`);
        if (approved != null) parts.push(`승인 ${esc(String(approved))}`);
        if (applied != null) parts.push(`신청 ${esc(String(applied))}`);
        if (parts.length) {
            const countsEl = document.createElement('div');
            countsEl.className = 'small text-muted mb-1';
            countsEl.textContent = parts.join(' / ');
            body.appendChild(countsEl);
        }
    }

    // 7) 하단 메타: 좌(가게·주소·평점) · 우(조회수/댓글: coeat만)
    const metaRow = document.createElement('div');
    metaRow.className = 'mt-auto d-flex justify-content-between align-items-center small text-muted';

    const leftMeta = document.createElement('div');
    const storeName =
        (item?.store?.name)
        ?? (item?.store?.id != null ? `store #${item.store.id}`
            : (item?.coeat?.storeId != null ? `store #${item.coeat.storeId}` : ''));
    const bits = [];
    if (storeName) bits.push(esc(storeName));
    const addr = item?.address?.roadAddressText;
    if (addr) bits.push(esc(addr));
    if (isReview && item?.review?.rating != null) {
        bits.push(`<span class="text-warning">★ ${esc(String(item.review.rating))}</span>`);
    }
    leftMeta.innerHTML = bits.filter(Boolean).join(' · ');

    const rightMeta = document.createElement('div');
    if (!isReview) {
        const partsR = [];
        if (item?.coeat?.viewCount != null) {
            partsR.push(`<i class="bi bi-eye me-1"></i>${esc(String(item.coeat.viewCount))}`);
        }
        if (item?.coeat?.commentCount != null) {
            partsR.push(`<i class="bi bi-chat-dots me-1"></i>${esc(String(item.coeat.commentCount))}`);
        }
        rightMeta.innerHTML = partsR.join(' · ');
    }

    metaRow.appendChild(leftMeta);
    metaRow.appendChild(rightMeta);
    body.appendChild(metaRow);

    card.appendChild(body);
    a.appendChild(card);
    return a;
}