request('get', '/');

function render() {
	const storeListContainer = document.querySelector('.storeList');
	const storeTemplate = document.getElementById('storelist-template');
	storeListContainer.innerHTML = '';
	state.stores.forEach(store => {
		const clone = document.importNode(storeTemplate.content, true);
		const link = clone.querySelector('a');
		link.href = `/store/detail/${store.id}`;
		clone.querySelector('.store-name').textContent = store.storeName;
        clone.querySelector('.store-category').textContent = store.category;
        clone.querySelector('.store-address').textContent = store.address;
        clone.querySelector('.store-rating-number').textContent = `${store.rating}점`;
        clone.querySelector('.store-reviews-count').textContent = store.reviewCount;
		const starContainer = clone.querySelector('.store-rating-star');
		starContainer.innerHTML = generateStars(store.rating);
		storeListContainer.appendChild(clone);
	});
}

function generateStars(rating) {
    let starsHTML = '';
	const roundedRating = Math.round(rating);
	for (let i = 1; i <= 5; i++) {
        if (i <= roundedRating) {
            starsHTML += '<i class="bi bi-star-fill" style="font-size: 1em; color: gold;"></i>';
        } else {
            starsHTML += '<i class="bi bi-star" style="font-size: 1em; color: lightgray;"></i>';
        }
    }
    return starsHTML;
}
