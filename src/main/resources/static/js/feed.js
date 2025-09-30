const feedState = {
    feeds: []
}

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

let fetchTimerId = null;
async function fetchFeed(){
		if(fetchTimerId) {
        clearTimeout(fetchTimerId);
    	}
	try{
		const response = await fetch('/feed');
		if(!response.ok){
			throw new Error('HTTP error! status: '+ response.status);
		}
		const newFeeds = await response.json();
		state.feeds = newFeeds;
		render();
	} catch (error){
		console.error("가게 목록 불러오기 실패.", error);
	} finally{
		fetchTimerId = setTimeout(fetchFeed, 30000);
	}
}