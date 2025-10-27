// /js/station/core/dom.js
export const esc = s => String(s ?? '').replace(/[&<>"']/g, c => ({
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#39;'
}[c]));
export const clip = (s, n = 60) => String(s ?? '').replace(/\s+/g, ' ').slice(0, n);
export const hrefOf = it => it.type === 'REVIEW' ? `/review/${encodeURIComponent(it.id)}` : `/coeat/${encodeURIComponent(it.id)}`;
