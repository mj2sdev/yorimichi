(() => {
    const listEl = document.getElementById('cat-list');
    if (!listEl) return;

    let nextId = listEl.dataset.nextId || null;
    let hasNext = (listEl.dataset.hasNext || 'false') === 'true';
    const size = parseInt(listEl.dataset.size || '10', 10);

    let inFlight = false;
    const sentinel = document.getElementById('cat-sentinel');

    async function loadMore() {
        if (inFlight || !hasNext) return;
        inFlight = true;

        const params = new URLSearchParams();
        if (nextId) params.set('categoryId', nextId);
        params.set('size', String(size));

        try {
            const res = await fetch(`/api/categories?${params.toString()}`, {headers: {'Accept': 'application/json'}});
            const ct = res.headers.get('content-type') || '';
            if (!res.ok || !ct.includes('application/json')) {
                console.error('[cat] API error:', res.status, await res.text());
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
            console.error('[cat] load fail:', e);
        } finally {
            inFlight = false;
        }
    }

    function append(items) {
        if (!Array.isArray(items) || !items.length) return;
        const frag = document.createDocumentFragment();
        for (const c of items) {
            const a = document.createElement('a');
            a.className = 'cat-btn';
            a.href = `/store/list?categoryId=${encodeURIComponent(c.id)}`;
            a.innerHTML = `<span>•</span><span class="fw-normal">${c.name}</span><i class="bi bi-chevron-right"></i>`;
            frag.appendChild(a);
        }
        listEl.insertBefore(frag, sentinel);
    }

    const observer = new IntersectionObserver((entries) => {
        for (const e of entries) if (e.isIntersecting) loadMore();
    }, {root: listEl, rootMargin: '0px 0px 200px 0px', threshold: 0.01});

    observer.observe(sentinel);
})();