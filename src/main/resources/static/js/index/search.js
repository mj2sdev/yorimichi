(() => {
  const ENDPOINT = '/api/search/suggest';
  const form = document.getElementById('search-form');
  const input = document.getElementById('search-input');
  const panel = document.getElementById('search-panel');
  const list = document.getElementById('search-list');
  const empty = panel?.querySelector('.search-empty');

  if (!form || !input || !panel || !list || !empty) return;

  let state = { items:[], activeIndex:-1, lastQ:'' };

  function openPanel(){ panel.classList.remove('d-none'); }
  function closePanel(){ panel.classList.add('d-none'); state.activeIndex=-1; render(); }

  function escapeRegExp(s){ return s.replace(/[.*+?^${}()|[\]\\]/g,'\\$&'); }
  function highlight(label, q){
    if(!q) return label;
    const re = new RegExp('('+escapeRegExp(q)+')','ig');
    return (label || '').replace(re, '<mark class="ym">$1</mark>');
  }

  function render(){
    list.innerHTML = '';
    if(!state.items.length){ empty.classList.remove('d-none'); return; }
    empty.classList.add('d-none');
    list.innerHTML = state.items.map((it, idx) => `
      <li class="search-item ${idx===state.activeIndex?'active':''}" data-idx="${idx}" role="option">
        <span class="type">${it.type}</span>
        <span class="label">${highlight(it.label, state.lastQ)}</span>
        ${it.sublabel ? `<span class="sublabel">${it.sublabel}</span>` : ''}
      </li>
    `).join('');
  }

  async function fetchSuggest(q){
    if(!q?.trim()){ state.items=[]; state.lastQ=''; render(); return; }
    try{
      const res = await fetch(`${ENDPOINT}?q=${encodeURIComponent(q)}`, { headers:{'Accept':'application/json'} });
      const items = res.ok ? await res.json() : [];
      state.items = Array.isArray(items) ? items.slice(0, 8) : [];
      state.lastQ = q;
      render(); openPanel();
    }catch(e){
      console.error('[search] suggest error', e);
      state.items=[]; render(); openPanel();
    }
  }
  const debounce = (fn, ms=180) => { let t; return (...a)=>{ clearTimeout(t); t=setTimeout(()=>fn(...a),ms); }; };

  input.addEventListener('focus', () => { if(state.items.length) openPanel(); });
  input.addEventListener('input', debounce(()=> fetchSuggest(input.value), 180));
  document.addEventListener('click', (e) => { if(!form.contains(e.target)) closePanel(); });

  list.addEventListener('click', (e)=>{
    const li = e.target.closest('.search-item');
    if(!li) return;
    const idx = +li.dataset.idx;
    goTo(state.items[idx]);
  });

  input.addEventListener('keydown', (e)=>{
    if(e.key === 'Enter'){
      // 선택된 항목 있으면 그것으로, 없으면 q만으로
      if(state.activeIndex>=0 && state.items.length){
        e.preventDefault();
        goTo(state.items[state.activeIndex]);
      }else{
        // 선택 없음 → 통합 검색
        e.preventDefault();
        goTo(); // item 없음
      }
    }else if(e.key === 'ArrowDown' && state.items.length){
      e.preventDefault();
      state.activeIndex = (state.activeIndex+1+state.items.length)%state.items.length;
      render();
    }else if(e.key === 'ArrowUp' && state.items.length){
      e.preventDefault();
      state.activeIndex = (state.activeIndex-1+state.items.length)%state.items.length;
      render();
    }else if(e.key==='Escape'){ closePanel(); }
  });


function goTo(item){
  if (!item) {
    // 그냥 검색 버튼/엔터 => 통합 검색
    const q = input.value.trim();
    if (!q) return;
    window.location.href = `/search?q=${encodeURIComponent(q)}`;
    return;
  }
  // 자동완성 선택 => 타입/아이디까지 붙여서 넘김
  const q = item.label || input.value.trim();
  const t = (item.type || '').toLowerCase();
  const id = item.id ? `&id=${encodeURIComponent(item.id)}` : '';
  window.location.href = `/search?q=${encodeURIComponent(q)}&type=${encodeURIComponent(t)}${id}`;
}

  // 🔸 검색 버튼 눌러도 선택 없이 q만으로 /search 이동
  form.addEventListener('submit', (e)=>{
    e.preventDefault();
    goTo(); // 선택 없이 이동
  });
})();
