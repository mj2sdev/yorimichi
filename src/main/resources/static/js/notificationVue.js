/// <reference path="./request.js"/>
const notificationOffcanvas = Vue.createApp({
	setup() {
		const notifications = Vue.ref([]);

		const findAll = async function() {
			const result = await request("GET", "/notifications", { param: {} });
			notifications.value = result;
			await Vue.nextTick();

			console.log(notifications.value.length);
			navigator.count = notifications.value.length;
		}

		const typeString = function(type) {
			const mapper = {
				"REVIEW": "리뷰",
				"COMMENT": "댓글",
				"COEAT": "같이먹기",
				"FOLLOW": "팔로우"
			}
			const result = mapper[type] || "";
			return result;
		}

		const readNotification = async function(id) {
			const result = await request("PATCH", `/notification/${id}`, { param: {} });
			if (result === true) {
				notifications.value.find(item => item.id == id).readAt = new Date().toISOString();
			} else {
				alert("既読の印をする途中にエラーが発生しました。");
			}
		}

		Vue.onMounted(findAll);

		return {
			notifications,
			readNotification,
			findAll,
			typeString
		}
	}
}).mount("#notification");

const navigator = Vue.createApp({
	setup() {
		const count = Vue.ref(0);

		return { count }
	}
}).mount("#notificationCount")