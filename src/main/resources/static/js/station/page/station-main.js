// /js/station/page/station-main.js
import {Cursor} from '../core/cursor.js';
import {buildUrl, fetchSlice} from '../core/api.js';
import {createInfiniteObserver} from '../core/infinite.js';
import {setupRelTimeTick} from '../core/time.js';
import {renderCardItem} from '../render/card-grid.js';

(() => {
    const ROOT = document.getElementById('station');
    const COL = ROOT?.querySelector('.masonry-col');
    const SENTINEL = document.getElementById('station-sentinel');
    if (!ROOT || !COL || !SENTINEL) return;

    const MasonryCtor = window.Masonry;
    const imagesLoaded = window.imagesLoaded;
    if (!MasonryCtor || !imagesLoaded) {
        console.error('[station] Masonry or imagesLoaded not found');
        return;
    }

    const ensureSizer = () => {
        let s = COL.querySelector('.grid-sizer');
        if (!s) {
            s = document.createElement('div');
            s.className = 'grid-sizer';
            COL.insertBefore(s, COL.firstChild);
        }
    };
    ensureSizer();

    const msnry = new MasonryCtor(COL, {
        itemSelector: '.grid-item',
        columnWidth: '.grid-sizer',
        percentPosition: true,
        transitionDuration: '0.2s',
        horizontalOrder: true
    });

    msnry.layout();
    imagesLoaded(COL, () => msnry.layout());

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

    let layoutLock = false;
    let lastKnownHeight = 0;
    let addedSinceFilter = 0; // ✅ 필터 전환 이후 DOM에 붙인 개수

    setupRelTimeTick(ROOT);

    // ✅ 몇 개 붙였는지 숫자 리턴
    const append = (items) => {
        if (!items?.length) return 0;

        const frag = document.createDocumentFragment();
        const elems = [];
        let appended = 0;

        for (const it of items) {
            // 프론트 필터: type이 다르면 스킵 (ALL이면 통과)
            if (currentFilter !== 'ALL' && it?.type !== currentFilter) continue;

            const key = String(it.id);
            if (seen.has(key)) continue;
            seen.add(key);

            const el = renderCardItem(it);
            elems.push(el);
            frag.appendChild(el);
            appended++;
        }
        if (!elems.length) return 0;

        COL.appendChild(frag);

        ensureSizer();
        msnry.appended(elems);
        msnry.layout();

        if (layoutLock) {
            layoutLock = false;
            COL.style.minHeight = '';
        }

        imagesLoaded(elems, () => msnry.layout());

        return appended;
    };

    // ✅ loadMore도 “붙인 개수”를 리턴
    const loadMore = async () => {
        if (inFlight || !cur.hasNext) return 0;
        inFlight = true;
        if (controller) controller.abort();
        controller = new AbortController();
        try {
            const params = {
                nextId: cur.nextId,
                size: cur.size,
                ...(currentFilter !== 'ALL' ? {type: currentFilter} : {}) // 서버가 지원하면 백필터도 사용
            };
            const url = buildUrl(endpoint, cursorParam, params);
            const slice = await fetchSlice(url, controller.signal);
            const appended = append(slice.items);
            cur.applySlice(slice);
            if (!cur.hasNext) io.unobserve(SENTINEL), SENTINEL.remove?.();
            return appended;
        } catch (e) {
            if (e.name !== 'AbortError') console.error('[station] fetch error:', e);
            return 0;
        } finally {
            inFlight = false;
        }
    };

    const isScrollable = (el) => {
        const cs = getComputedStyle(el);
        const ov = cs.overflowY;
        return (ov === 'auto' || ov === 'scroll') && el.scrollHeight > el.clientHeight;
    };

    // ✅ 필터일 때 최소 N개 이상 채우기
    const fillAtLeast = async (minItems = cur.size, maxLoops = 10) => {
        let loops = 0;
        while (addedSinceFilter < minItems && cur.hasNext && loops < maxLoops) {
            const added = await loadMore();
            if (added === 0) loops++; else loops = 0; // 진전 없으면 루프 카운트
            addedSinceFilter += added;
        }
    };

    const io = createInfiniteObserver(
        ROOT,
        SENTINEL,
        () => loadMore(),
        {root: ROOT, rootMargin: '0px 0px 300px 0px', threshold: 0.01}
    );

    const nearBottom = () => (ROOT.scrollTop + ROOT.clientHeight) >= (ROOT.scrollHeight - 240);
    ROOT.addEventListener('scroll', () => {
        if (nearBottom()) loadMore();
    }, {passive: true});

    const ensureScrollable = async (tries = 3) => {
        let n = 0;
        while (!isScrollable(ROOT) && cur.hasNext && n < tries) {
            const added = await loadMore();
            if (added === 0) n++; // 진전 없으면 시도 카운트 증가
            await new Promise(r => requestAnimationFrame(r));
        }
    };

    // 초기 프리로드: ALL 기준으로 먼저 채움
    (async () => {
        addedSinceFilter = 0;
        addedSinceFilter += await loadMore();
        await ensureScrollable(3);
        const r = SENTINEL.getBoundingClientRect();
        const vh = ROOT.clientHeight;
        if (r.top < vh + 50 && cur.hasNext) addedSinceFilter += await loadMore();
    })();

    // ✅ 필터 변경: 최소 cur.size 개수는 채워 넣기
    document.addEventListener('stationFilterChange', async (ev) => {
        const next = (ev.detail?.type || 'ALL');
        if (next === currentFilter) return;

        currentFilter = next;
        cur.reset();
        seen.clear();
        addedSinceFilter = 0;
        if (controller) controller.abort();

        // 레이아웃 잠금
        lastKnownHeight = COL.clientHeight || 0;
        if (lastKnownHeight > 0) {
            layoutLock = true;
            COL.style.minHeight = lastKnownHeight + 'px';
        }

        // 기존 아이템 제거 (sizer 유지)
        ensureSizer();
        const existing = COL.querySelectorAll('.grid-item');
        if (existing.length) {
            existing.forEach(n => n.remove());     // DOM 제거
            msnry.reloadItems();                   // Masonry 내부 리스트 갱신
            msnry.layout();
        }

        // 스크롤/센티널 재설정
        ROOT.scrollTop = 0;
        io.unobserve(SENTINEL);
        ROOT.appendChild(SENTINEL);
        io.observe(SENTINEL);

        // 최소 cur.size 개 이상 채워 넣기 (COEAT가 드문 경우 대비)
        await fillAtLeast(cur.size, 12);

        // 그래도 스크롤이 부족하면 보충
        await ensureScrollable(3);

        const r = SENTINEL.getBoundingClientRect();
        if (r.top < ROOT.clientHeight + 50 && cur.hasNext) {
            addedSinceFilter += await loadMore();
        }
    }, {passive: true});
})();