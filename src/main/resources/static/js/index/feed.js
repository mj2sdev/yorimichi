function render(){
    const feedListContainer = document.querySelector('.feed');
    const feedTemplate = document.getElementById('feed-template');
    feedListContainer.innerHTML = '';
    feedState.feeds.forEach(feed => {
        const clone = document.importNode(feedTemplate.contentEditable,true);
        clone.querySelector('feed-address').textContent = feed.store.address
        clone.querySelector('feed-store-name').textContent = feed.store.name
        clone.querySelector('feed-store-category').textContent = feed.store.category
        clone.querySelector('feed-feed-category').textContent = feed.category
        feedListContainer.appendChild(clone);
    })
}
