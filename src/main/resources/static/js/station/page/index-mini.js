// /js/station/page/index-mini.js
import {Cursor} from '../core/cursor.js';
import {buildUrl, fetchSlice} from '../core/api.js';
import {createInfiniteObserver} from '../core/infinite.js';
import {setupRelTimeTick} from '../core/time.js';
import {renderMiniItem} from '../render/list-mini.js';

(() => {
    const ROOT = document.getElementById('station');
    const SENTINEL = document.getElementById('station-sentinel');
    if (!ROOT || !SENTINEL) return;

    const endpoint = ROOT.dataset.endpoint || '/api/stations';
    const cursorParam = 'nextId';

    const cur = new Cursor({
        nextId: ROOT.dataset.nextId || null,
        hasNext: (ROOT.dataset.hasNext || 'false') === 'true',
        size: parseInt(ROOT.dataset.size || '6', 10)
    });

    const seen = new Set(Array.from(ROOT.querySelectorAll('[list-id]')).map(n => n.getAttribute('list-id')));
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
            frag.appendChild(renderMiniItem(it));
        }
        ROOT.insertBefore(frag, SENTINEL);
    };

    const loadMore = async () => {
        if (inFlight || !cur.hasNext) return;
        inFlight = true;
        if (controller) controller.abort();
        controller = new AbortController();
        try {
            const url = buildUrl(endpoint, cursorParam, {nextId: cur.nextId, size: cur.size});
            const slice = await fetchSlice(url, controller.signal);
            append(slice.items);
            cur.applySlice(slice);
            if (!cur.hasNext) io.unobserve(SENTINEL), SENTINEL.remove?.();
        } catch (e) {
            if (e.name !== 'AbortError') {
                console.error('[mini] fetch error:', e);
                io.unobserve(SENTINEL);
            }
        } finally {
            inFlight = false;
        }
    };

    const isScrollable = (el) => {
        const cs = getComputedStyle(el);
        const ov = cs.overflowY;
        return (ov === 'auto' || ov === 'scroll') && el.scrollHeight > el.clientHeight;
    };
    const rootOpt = isScrollable(ROOT) ? ROOT : null;
    console.debug('[mini] init', {scrollable: !!rootOpt});

    const io = createInfiniteObserver(rootOpt, SENTINEL, () => {
        console.debug('[mini] IO → loadMore');
        loadMore();
    }, {root: rootOpt, rootMargin: '0px 0px 200px 0px', threshold: 0.01});

    const sentinelRect = SENTINEL.getBoundingClientRect();
    const vh = window.innerHeight || document.documentElement.clientHeight;
    if (rootOpt === null && sentinelRect.top < vh + 50) {
        console.debug('[mini] pre-loadMore (sentinel visible at init)');
        loadMore();
    }

    if (cur.hasNext && (rootOpt !== null || window.innerHeight > 700)) {
        loadMore();
    }
})();