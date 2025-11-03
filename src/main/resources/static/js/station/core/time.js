// /js/station/core/time.js
export function timeAgo(iso) {
    try {
        const d = (Date.now() - new Date(iso).getTime()) / 1000;
        if (d < 60) return '先ほと';
        if (d < 3600) return Math.floor(d / 60) + '分前';
        if (d < 86400) return Math.floor(d / 3600) + '時間前';
        return Math.floor(d / 86400) + '日前';
    } catch {
        return '';
    }
}

export function setupRelTimeTick(root, intervalMs = 60000) {
    const tick = () => {
        root.querySelectorAll('.rel-time[data-iso]').forEach(el => {
            const iso = el.dataset.iso;
            if (iso) el.textContent = timeAgo(iso);
        });
    };
    document.readyState === 'loading' ? document.addEventListener('DOMContentLoaded', tick) : tick();
    return setInterval(tick, intervalMs);
}
