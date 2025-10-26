// /js/station/core/infinite.js
export function createInfiniteObserver(rootEl, sentinelEl, onIntersect, opts = {}) {
    const io = new IntersectionObserver((entries) => {
        for (const e of entries) if (e.isIntersecting) onIntersect();
    }, {
        root: opts.root ?? null,
        rootMargin: opts.rootMargin ?? '0px 0px 200px 0px',
        threshold: opts.threshold ?? 0.01
    });
    io.observe(sentinelEl);
    return io;
}
