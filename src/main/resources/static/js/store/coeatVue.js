/// <reference path="../request.js"/>

const { createApp, ref, nextTick } = Vue;
const coeatDetailModal = createApp({
	setup() {
		const coeat = ref("");
		const content = ref("");

		const calculateAveragePrice = function(array) {
			if (!array || !array.length) return 0;
			const total = array.map(food => food.price).reduce((acc, val) => acc + val);
			const averagePrice = total / array.length;
			return averagePrice;
		}
		const findCoeat = async function(coeatId) {
			const result = await request("GET", `/coeat/${coeatId}`);

			coeat.value = result;
			console.log(coeat);
			await nextTick();
			timeAgo();
		}
		const findOne = async function(event) {
			const { currentTarget } = event;
			const coeatId = currentTarget.dataset.id;
			findCoeat(coeatId);
		}
		const writeComment = async function(event) {
			event.preventDefault();
			const { currentTarget } = event;
			const coeatId = currentTarget.coeatId.value;
			const body = { content: content.value, coeatId };
			const result = await request("POST", "/comment", { body });

			if (result === true) {
				findCoeat(coeat?.value?.id);
				content.value = "";
			} else {
				alert("コメントを書く途中エラーは発生しました。");
			}
		}

		const requestCoeat = async function(event) {
			if (!confirm("参加を申し込みますか?")) return;
			const coeatId = coeat?.value?.id;
			const result = await request("POST", `/coeat/${coeatId}/participant`, { body: {} });
			
			if (result === true) {
				alert("参加申請が完了しました。");
			} else {
				alert("参加申請中にエラーが発生しました。");
			}
		}

		return { coeat, content, findOne, calculateAveragePrice, writeComment, requestCoeat }
	},
	
}).mount("#coeatmodal")