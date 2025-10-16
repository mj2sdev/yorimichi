/* ============================================================================
 * 인기 맛집 리스트 (하단 3×3) + 중앙 Featured 보조 업데이트
 * - 서버가 Thymeleaf로 이미 렌더했다면 JS는 아무 것도 하지 않아도 OK.
 * - 컨테이너가 비어 있으면 API를 시도해서 비동기 렌더링.
 * - API 경로는 환경에 따라 다를 수 있어 두 경로를 순차 시도(404 대비).
 * - 이미지 없으면 picsum 더미 사용, lazy-load 적용.
 * ============================================================================ */
(function () {
  const GRID_SELECTOR = '.store-grid';
  const FEATURED_IMAGE_SELECTOR = '.featured .thumb';
  const TRY_ENDPOINTS = [
    '/api/stores/recommended?count=9',
    '/store/recommended?count=9'
  ];
  const REFRESH_MS = 0; // 자동 갱신 비활성(원하면 60000 등으로)
  const FALLBACK_IMAGES = [
    'https://picsum.photos/seed/yorimichi1/600/400',
    'https://picsum.photos/seed/yorimichi2/600/400',
    'https://picsum.photos/seed/yorimichi3/600/400',
    'https://picsum.photos/seed/yorimichi4/600/400',
    'https://picsum.photos/seed/yorimichi5/600/400',
    'https://picsum.photos/seed/yorimichi6/600/400',
    'https://picsum.photos/seed/yorimichi7/600/400',
    'https://picsum.photos/seed/yorimichi8/600/400',
    'https://picsum.photos/seed/yorimichi9/600/400'
  ];

  let observer;

  document.addEventListener('DOMContentLoaded', init);

  async function init() {
    const grid = document.querySelector(GRID_SELECTOR);
    if (!grid) return;

    // 이미 서버가 채워줬다면 패스
    if (grid.children.length > 0) {
      initLazyImages(grid);
      return;
    }

    // 스켈레톤
    grid.innerHTML = skeletonCards(9);

    await refreshOnce(grid);

    if (REFRESH_MS > 0) {
      setInterval(() => refreshOnce(grid), REFRESH_MS);
    }
  }

  async function refreshOnce(grid) {
    const data = await fetchRecommended();
    if (!Array.isArray(data) || data.length === 0) {
      grid.innerHTML = `<div class="text-center text-muted">아직 인기 상점 데이터가 없습니다.</div>`;
      return;
    }

    grid.innerHTML = data.slice(0, 9).map((store, idx) => renderStoreCard(store, idx)).join('');
    initLazyImages(grid);

    // Featured 이미지(중앙 카드) 보조 업데이트: heroImageUrl이 없을 때만
    const featuredImg = document.querySelector(FEATURED_IMAGE_SELECTOR);
    if (featuredImg && featuredImg.classList.contains('skeleton')) {
      featuredImg.src = fallbackImage(0);
      featuredImg.classList.remove('skeleton');
    }
  }

  async function fetchRecommended() {
    for (const url of TRY_ENDPOINTS) {
      try {
        const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
        if (res.ok) {
          return await res.json();
        }
      } catch (e) {
        // 다음 엔드포인트 시도
        console.warn('[storelist] fetch fallback:', url, e);
      }
    }
    return [];
  }

  function renderStoreCard(store, idx) {
    const id = store?.id ?? idx + 1;
    const name = store?.name ?? `요리미치 이자카야 ${idx + 1}`;
    const addr = (store?.address?.roadAddressText) || '주소 없음';
    const desc = store?.description || '설명 정보가 없습니다.';
    const phone = store?.phone || '전화번호 없음';
    const href = `/store/detail?id=${id}`;

    // 이미지 우선순위: window.__IMAGE_URLS__ → store.images[0].url → picsum
    const imageUrl = pickImageUrl(store, idx);

    return `
      <a href="${href}" class="text-decoration-none">
        <div class="store-card">
          <img class="thumb lazy" data-src="${imageUrl}" alt="store image">
          <div class="name">${escapeHtml(name)}</div>
          <div class="addr">${escapeHtml(addr)}</div>
          <div class="desc">${escapeHtml(desc)}</div>
          <div class="d-flex align-items-center gap-2 mt-2">
            <span class="star">★</span><span class="text-muted small">4.5</span>
            <span class="text-muted small">·</span><span class="text-muted small">123 리뷰</span>
          </div>
        </div>
      </a>`;
  }

  function pickImageUrl(store, idx) {
    // 1) 서버가 index에 내려준 이미지 배열(Thymeleaf에서 window.__IMAGE_URLS__ 주입 가능)
    if (Array.isArray(window.__IMAGE_URLS__) && window.__IMAGE_URLS__.length > 0) {
      return window.__IMAGE_URLS__[idx % window.__IMAGE_URLS__.length];
    }
    // 2) store.images[0].url
    if (store?.images && Array.isArray(store.images) && store.images.length > 0) {
      const first = store.images[0];
      if (first?.url) return first.url;
    }
    // 3) fallback
    return fallbackImage(idx);
  }

  function fallbackImage(i) {
    return FALLBACK_IMAGES[i % FALLBACK_IMAGES.length];
  }

  // --------- Lazy Load ---------
  function initLazyImages(root) {
    const imgs = root.querySelectorAll('img.lazy');
    if (imgs.length === 0) return;

    if ('IntersectionObserver' in window) {
      observer?.disconnect?.();
      observer = new IntersectionObserver(onIntersect, { rootMargin: '120px 0px' });
      imgs.forEach(img => observer.observe(img));
    } else {
      // 폴백: 즉시 로드
      imgs.forEach(loadImg);
    }
  }
  function onIntersect(entries, obs) {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        loadImg(entry.target);
        obs.unobserve(entry.target);
      }
    });
  }
  function loadImg(img) {
    const src = img.getAttribute('data-src');
    if (src) {
      img.src = src;
      img.removeAttribute('data-src');
      img.classList.remove('lazy');
    }
  }

  // --------- Skeleton cards ---------
  function skeletonCards(n) {
    return Array.from({ length: n }).map(() => `
      <div class="store-card">
        <div class="skeleton" style="height:180px;border-radius:.8rem;"></div>
        <div class="skeleton mt-2" style="height:16px;width:60%;border-radius:8px;"></div>
        <div class="skeleton mt-2" style="height:12px;width:80%;border-radius:6px;"></div>
        <div class="skeleton mt-2" style="height:12px;width:40%;border-radius:6px;"></div>
      </div>`).join('');
  }

  // -------- utils --------
  function escapeHtml(s) {
    return String(s ?? '').replace(/[&<>"']/g, m => ({
      '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
    })[m]);
  }
})();
