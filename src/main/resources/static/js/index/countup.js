// 카운트업 모듈 (DOM 준비되면 자동 실행)
(() => {
    const DURATION = 1200;
    const STAGGER = 120;
    const EASE = t => 1 - Math.pow(1 - t, 3);

    const fmt = (n, el) => el?.dataset.format === 'comma' ? n.toLocaleString() : String(n);

    function run(el, delay = 0) {
        const target = Number(el.dataset.value || el.textContent || 0);
        if (!isFinite(target)) return;
        const start = performance.now() + delay;
        const from = 0;
        const dec = Math.pow(10, (el.dataset.decimals | 0));
        const duration = Number(el.dataset.duration || DURATION);

        function tick(now) {
            if (now < start) return requestAnimationFrame(tick);
            const t = Math.min(1, (now - start) / duration);
            const eased = EASE(t);
            const current = Math.round((from + (target - from) * eased) * dec) / dec;
            el.textContent = fmt(current, el);
            if (t < 1) requestAnimationFrame(tick);
            else el.textContent = fmt(target, el);
        }

        requestAnimationFrame(tick);
    }

    function boot() {
        const els = Array.from(document.querySelectorAll('.countup')).filter(el => !el.dataset.counted);
        if (!els.length) return;

        if ('IntersectionObserver' in window) {
            const io = new IntersectionObserver(entries => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        const idx = els.indexOf(entry.target);
                        run(entry.target, idx * STAGGER);
                        entry.target.dataset.counted = '1';
                        io.unobserve(entry.target);
                    }
                });
            }, {threshold: 0.6});
            els.forEach(el => io.observe(el));
        } else {
            els.forEach((el, i) => run(el, i * STAGGER));
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', boot);
    } else {
        boot();
    }
})();