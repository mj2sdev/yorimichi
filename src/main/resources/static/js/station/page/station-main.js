// /js/station/page/station-main.js
import {Cursor} from '../core/cursor.js';
import {buildUrl, fetchSlice} from '../core/api.js';
import {createInfiniteObserver} from '../core/infinite.js';
import {setupRelTimeTick} from '../core/time.js';
import {renderCardItem} from '../render/card-grid.js';

(() => {
    const ROOT = document.getElementById('station');
    const COL = ROOT?.querySelector('.masonry-col');   // 컬럼 컨테이너
    const SENTINEL = document.getElementById('station-sentinel');
    if (!ROOT || !COL || !SENTINEL) return;

    const endpoint = ROOT.dataset.endpoint || '/api/stations';
    const cursorParam = ROOT.dataset.cursorParam || 'nextId';

    const cur = new Cursor({
        nextId: ROOT.dataset.nextId || null,
        hasNext: (ROOT.dataset.hasNext || 'false') === 'true',
        size: parseInt(ROOT.dataset.size || '12', 10)
    });

    const seen = new Set(Array.from(ROOT.querySelectorAll('[list-id]')).map(n => n.getAttribute('list-id')));
    let currentFilter = 'ALL';
    let inFlight = false;
    let controller = null;

    setupRelTimeTick(ROOT);

    const append = (items) => {
        if (!items?.length) return;
        const frag = document.createDocumentFragment();
        for (const it of items) {
            const key = String(it.id);
            if (seen.has(key)) continue;
            seen.add(key);
            frag.appendChild(renderCardItem(it)); // <a> 또는 <div.card>
        }
        COL.appendChild(frag);
    };

    const loadMore = async () => {
        if (inFlight || !cur.hasNext) return;
        inFlight = true;
        if (controller) controller.abort();
        controller = new AbortController();
        try {
            const params = {
                nextId: cur.nextId,
                size: cur.size, ...(currentFilter !== 'ALL' ? {type: currentFilter} : {})
            };
            const url = buildUrl(endpoint, cursorParam, params);
            const slice = await fetchSlice(url, controller.signal);
            append(slice.items);
            cur.applySlice(slice);
            if (!cur.hasNext) io.unobserve(SENTINEL), SENTINEL.remove?.();
        } catch (e) {
            if (e.name !== 'AbortError') console.error('[station] fetch error:', e);
        } finally {
            inFlight = false;
        }
    };

    const isScrollable = (el) => {
        const cs = getComputedStyle(el);
        const ov = cs.overflowY;
        return (ov === 'auto' || ov === 'scroll') && el.scrollHeight > el.clientHeight;
    };

    // IO: 루트는 항상 #station
    const io = createInfiniteObserver(
        ROOT,
        SENTINEL,
        () => {
            loadMore();
        },
        {root: ROOT, rootMargin: '0px 0px 300px 0px', threshold: 0.01}
    );

    // near-bottom 폴백
    const nearBottom = () => (ROOT.scrollTop + ROOT.clientHeight) >= (ROOT.scrollHeight - 240);
    ROOT.addEventListener('scroll', () => {
        if (nearBottom()) loadMore();
    }, {passive: true});

    // 스크롤이 생길 때까지 강제 프리로드
    const ensureScrollable = async (tries = 3) => {
        let n = 0;
        while (!isScrollable(ROOT) && cur.hasNext && n < tries) {
            await loadMore();
            await new Promise(r => requestAnimationFrame(r));
            n++;
        }
    };

    // 초기 프리로드
    (async () => {
        await loadMore();
        await ensureScrollable(3);
        // 센티널이 보이면 추가 1회
        const r = SENTINEL.getBoundingClientRect();
        const vh = ROOT.clientHeight;
        if (r.top < vh + 50 && cur.hasNext) await loadMore();
    })();

    // 필터 변경
    document.addEventListener('stationFilterChange', (ev) => {
        const {type} = ev.detail || {};
        currentFilter = type || 'ALL';
        cur.reset();
        COL.innerHTML = '';
        seen.clear();
        if (controller) controller.abort();
        io.unobserve(SENTINEL);
        ROOT.appendChild(SENTINEL); // 센티널을 항상 루트 맨아래로
        io.observe(SENTINEL);
        loadMore();
    }, {passive: true});
})();