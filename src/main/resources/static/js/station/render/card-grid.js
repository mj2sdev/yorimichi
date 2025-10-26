import {esc, hrefOf} from '../core/dom.js';
import {timeAgo} from '../core/time.js';

export function renderCardItem(item) {
    const isReview = item?.type === 'REVIEW';

    const a = document.createElement('a');
    a.className = 'text-decoration-none';
    a.setAttribute('list-id', item?.id);
    a.dataset.type = item?.type ?? '';
    a.href = hrefOf(item);

    const card = document.createElement('div');
    card.className = 'card mb-3 shadow-sm border-0';

    const body = document.createElement('div');
    body.className = 'card-body p-3';

    // 헤더: 타입/뱃지 + 시간 (조회수 제거)
    const head = document.createElement('div');
    head.className = 'd-flex align-items-start justify-content-between mb-2';

    const left = document.createElement('div');
    left.className = 'd-flex align-items-center gap-2';
    left.innerHTML = `
    <i class="bi fs-5 ${isReview ? 'bi-chat-square-text-fill text-primary' : 'bi-people-fill text-warning'}"></i>
    <span class="badge rounded-pill small ${isReview ? 'bg-primary bg-opacity-10 text-primary' : 'bg-warning bg-opacity-10 text-warning'}">
      ${isReview ? '리뷰' : '같이먹기'}
    </span>
  `;

    const right = document.createElement('div');
    right.className = 'd-flex align-items-center gap-2';

    const time = document.createElement('small');
    time.className = 'text-muted rel-time';
    time.dataset.iso = item?.createdAt ?? '';
    time.textContent = timeAgo(item?.createdAt);
    right.appendChild(time);

    head.appendChild(left);
    head.appendChild(right);

    // 대표 이미지
    const imgUrl = item?.image?.url;
    if (imgUrl) {
        const imgEl = document.createElement('img');
        imgEl.className = 'img-fluid rounded mb-2';
        imgEl.src = imgUrl;
        imgEl.alt = '';
        imgEl.loading = 'lazy';
        body.appendChild(head);
        body.appendChild(imgEl);
    } else {
        body.appendChild(head);
    }

    // 사용자명
    const user = document.createElement('div');
    user.className = 'mb-2';
    user.innerHTML = `<span class="fw-semibold text-dark small"><i class="bi bi-person-circle me-1"></i>${esc(item?.user?.nickname ?? '익명')}</span>`;
    body.appendChild(user);

    // 제목/본문
    const preview = document.createElement('div');
    preview.className = 'text-ymsecondary small mb-2 lh-base';
    Object.assign(preview.style, {
        display: '-webkit-box',
        WebkitLineClamp: '2',
        WebkitBoxOrient: 'vertical',
        overflow: 'hidden'
    });
    preview.textContent = isReview ? (item?.review?.content ?? '') : (item?.coeat?.title ?? '');
    body.appendChild(preview);

    // 같이먹기 본문(coeat.content)
    if (!isReview) {
        const coContent = item?.coeat?.content ?? '';
        if (coContent) {
            const coContentEl = document.createElement('div');
            coContentEl.className = 'text-muted small mb-2';
            Object.assign(coContentEl.style, {
                display: '-webkit-box',
                WebkitLineClamp: '3',
                WebkitBoxOrient: 'vertical',
                overflow: 'hidden'
            });
            coContentEl.textContent = coContent;
            body.appendChild(coContentEl);
        }
    }

    // 리뷰: 음식 배지
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

    // 같이먹기: 정원/승인/신청 (상점/주소 위)
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

    // ...중략...

    // 하단 메타: 좌(가게·주소·평점) · 우(조회수)
    const metaRow = document.createElement('div');
    metaRow.className = 'd-flex justify-content-between align-items-center small text-muted';

    const metaLeft = document.createElement('div');
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

    metaLeft.innerHTML = bits.filter(Boolean).join(' · ');

    const metaRight = document.createElement('div');
    if (!isReview && item?.coeat?.viewCount != null) {
        metaRight.innerHTML = `<i class="bi bi-eye me-1"></i>${esc(String(item.coeat.viewCount))}`;
    }

    metaRow.appendChild(metaLeft);
    metaRow.appendChild(metaRight);
    body.appendChild(metaRow);

    card.appendChild(body);
    a.appendChild(card);
    return a;
}