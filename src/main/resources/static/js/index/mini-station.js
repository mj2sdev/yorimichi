(() => {
    const BOX = document.getElementById('mini-station');
    if (!BOX) return;

    let nextId = BOX.dataset.nextId || null;
    let hasNext = (BOX.dataset.hasNext || 'false') === 'true';
    const size = parseInt(BOX.dataset.size || '6', 10);

    const sentinel = document.getElementById('station-sentinel');
    let inFlight = false;

    const seen = new Set(Array.from(BOX.querySelectorAll('[list-id]')).map(n => n.getAttribute('list-id')));

    function timeAgo(iso) {
        try {
            const t = new Date(iso).getTime(), d = (Date.now() - t) / 1000;
            if (d < 60) return '방금';
            if (d < 3600) return Math.floor(d / 60) + '분 전';
            if (d < 86400) return Math.floor(d / 3600) + '시간 전';
            return Math.floor(d / 86400) + '일 전';
        } catch {
            return '';
        }
    }

    async function loadMore() {
        if (inFlight || !hasNext) return;
        inFlight = true;

        const params = new URLSearchParams();
        if (nextId) params.set('rootId', nextId);
        params.set('size', String(size));

        try {
            const res = await fetch(`/api/mini-stations?${params.toString()}`, {headers: {'Accept': 'application/json'}});
            const ct = res.headers.get('content-type') || '';
            if (!res.ok || !ct.includes('application/json')) {
                console.error('[mini-station] API error:', res.status, await res.text());
                return;
            }
            const json = await res.json();
            append(json.items || []);
            nextId = json.nextId ?? null;
            hasNext = !!json.hasNext;
            if (!hasNext) {
                observer.disconnect();
                sentinel?.remove();
            }
        } catch (e) {
            console.error('[mini-station] fetch error:', e);
        } finally {
            inFlight = false;
        }
    }

    function append(items) {
        if (!Array.isArray(items) || !items.length) return;
        const frag = document.createDocumentFragment();

        for (const item of items) {
            const idKey = String(item.id);
            if (seen.has(idKey)) continue;
            seen.add(idKey);

            const isReview = item.type === 'REVIEW';
            const href = isReview ? `/review/${encodeURIComponent(item.id)}` : `/coeat/${encodeURIComponent(item.id)}`;

            const a = document.createElement('a');
            a.className = 'list-group-item list-group-item-action d-flex gap-2 align-items-start';
            a.setAttribute('list-id', idKey);
            a.dataset.type = item.type;
            a.href = href;

            a.innerHTML = `
        <i class="bi ${isReview ? 'bi-chat-dots' : 'bi-people'} mt-1"></i>
        <div class="flex-grow-1">
          <div class="d-flex justify-content-between">
            <div class="fw-semibold">${isReview ? '리뷰' : '같이먹기'}</div>
            <div class="text-muted small">${timeAgo(item.createdAt)}</div>
          </div>
          <div class="text-ymsecondary small">${String(isReview ? (item.content ?? '') : (item.title ?? '')).replace(/\s+/g, ' ').slice(0, 60)}</div>
          <div class="small text-muted">
            ${[item.name ?? (item.storeId != null ? `store #${item.storeId}` : ''), (isReview && item.rating != null) ? `★ ${item.rating}` : ''].filter(Boolean).join(' · ')}
          </div>
        </div>`;
            frag.appendChild(a);
        }
        BOX.insertBefore(frag, sentinel);
    }

    // 상대시간 주기 갱신(SSR된 .rel-time도 커버)
    const tick = () => {
        BOX.querySelectorAll('.rel-time[data-iso]').forEach(el => {
            const iso = el.dataset.iso;
            if (iso) el.textContent = timeAgo(iso);
        });
    };
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', tick);
    } else {
        tick();
    }
    setInterval(tick, 60_000);

    const observer = new IntersectionObserver((entries) => {
        for (const e of entries) if (e.isIntersecting) loadMore();
    }, {root: BOX, rootMargin: '0px 0px 200px 0px', threshold: 0.01});

    observer.observe(sentinel);
})();
