// @ts-check
/// <reference path="../request.js" />

/**
 * 좋아요 버튼을 토글합니다.
 * @author mj2sdev
 * @version 1.0 초안 작성
 * @param {Event} event 
 * @returns {Promise<void>}
 */
async function toggleLike(event) {
	const button = /** @type {HTMLButtonElement} */ (event.currentTarget);
	const liked = button.dataset.liked === "true";
	const method = liked ? "DELETE" : "POST";
	const storeId = location.pathname.split("/").pop();
	const url = "/store/like/" + storeId;
	const result = await request(method, url);

	if (result) {
		const i = button.querySelector("i");
		i?.classList.toggle("bi-heart");
		i?.classList.toggle("bi-heart-fill");
		button.dataset.liked = String(!liked);

		const flag = liked ? "キャンセル" : "設定";
		alert(`いいねが ${ flag } されました`)
	}
}
/**
 * 북마크 설정을 토글합니다.
 * 
 * @author mj2sdev
 * @version 1.0
 * @param {Event} event 
 * @returns {Promise<void>}
 */
async function toggleBookmark(event) {
	const button = /** @type {HTMLButtonElement} */ (event.currentTarget);
	const marked = button.dataset.marked === "true";
	const method = marked ? "DELETE" : "POST";
	const storeId = location.pathname.split("/").pop();
	const url = "/store/bookmark/" + storeId;
	const result = await request(method, url);

	if (result) {
		const i = button.querySelector("i");
		i?.classList.toggle("bi-bookmark");
		i?.classList.toggle("bi-bookmark-fill");
		button.dataset.marked = String(!marked);

		const flag = marked ? "キャンセル" : "設定";
		alert(`店のブックマーク ${ flag } されました`);
	}
}
/**
 * 
 * 브라우저 기본 쉐어 기능을 사용합니다.
 * @author mj2sdev
 * @version 1.0
 * @see navigator
 */
function shareStore() {
	if (navigator.share != null) {
		navigator.share({
			title: "店を共有します",
			text: "おはよう",
			url: location.href,
		})
	} else {
		alert("このブラウザは Share API をサポートしていません");
	}
}