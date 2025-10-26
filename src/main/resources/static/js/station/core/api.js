export function buildUrl(endpoint, cursorParam, {nextId, size, extra = {}}) {
    const p = new URLSearchParams();
    if (nextId) p.set(cursorParam, String(nextId));
    p.set('size', String(size));
    Object.entries(extra).forEach(([k, v]) => v != null && p.set(k, String(v)));
    return `${endpoint}?${p.toString()}`;
}

export async function fetchSlice(url, signal) {
    const res = await fetch(url, {headers: {'Accept': 'application/json'}, signal});
    const ct = res.headers.get('content-type') || '';
    if (!res.ok || !ct.includes('application/json')) {
        throw new Error(`API ${res.status}`);
    }
    const json = await res.json(); // { items, nextId, hasNext, size }
    return {
        items: Array.isArray(json.items) ? json.items : [],
        nextId: json.nextId ?? null,
        hasNext: !!json.hasNext,
        size: parseInt(json.size || 0, 10) || undefined
    };
}
