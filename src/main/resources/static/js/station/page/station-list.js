// /js/station/page/station-list.js
(() => {
    document.addEventListener('DOMContentLoaded', () => {
        const filterTabs = document.querySelectorAll('.filter-tab');
        if (!filterTabs.length) return;

        filterTabs.forEach(tab => {
            tab.addEventListener('click', (e) => {
                e.preventDefault();

                // active 토글
                filterTabs.forEach(t => t.classList.remove('active'));
                tab.classList.add('active');

                // 필터 이벤트 발행 (station-main.js가 수신)
                const type = tab.dataset.filter || 'ALL';
                const ev = new CustomEvent('stationFilterChange', {detail: {type}});
                document.dispatchEvent(ev);
            }, {passive: false});
        });
    });
})();