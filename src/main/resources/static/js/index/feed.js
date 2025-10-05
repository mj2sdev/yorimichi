(function(){
    async function updateFeed(){
        const feeds = await request('get', '/feed/{count}',{params:{count:10}});
        renderFeeds(feeds)
    }

    function renderFeeds(feeds){
        const feedListContainer = document.querySelector('.feed');
        const feedTemplate = document.getElementById('feed-template');
        feedListContainer.innerHTML = '';
        feeds.forEach(feed => {
            const clone = document.importNode(feedTemplate.textContent,true);
            clone.querySelector('.feed-address').textContent = feed.store.address
            clone.querySelector('.feed-store-name').textContent = feed.store.name
            clone.querySelector('.feed-store-category').textContent = feed.store.category
            clone.querySelector('.feed-feed-category').textContent = feed.category
            feedListContainer.appendChild(clone);
        })
    }

    updateFeed();
    setInterval(updateFeed, 15000);
})();
