"use strict";


/**
 * 이 함수는 bootstrap > form > validation 에 있는 부트스트랩 기본 발리데이션을 활성화 하는 함수입니다.
 * 페이지 로딩 후 실행되어야 합니다.
 */
function bsNeedValidation() {
	const forms = document.querySelectorAll(".needs-validation");

	Array.from(forms).forEach(form => {
		form.addEventListener("submit", event => {
			if (!form.checkValidity()) {
				event.preventDefault()
				event.stopPropagation();
			}
			form.classList.add("was-validated");
		}, false)
	})
}
bsNeedValidation();

/**
 * 시간 포멧 함수
 */
function timeAgo() {
	const timeElements = document.querySelectorAll("[data-time-ago]");
	timeElements.forEach(element => {
		const { timeAgo } = element.dataset;
		let message = "";
		try {
			const time = new Date(timeAgo).getTime();
			const nokori = (Date.now() - time) / 1000;
			if (nokori < 60) message = "방금";
			else if (nokori < 3600) message = Math.floor(nokori / 60) + "분 전";
			else if (nokori < 86400) message = Math.floor(nokori / 3600) + "시간 전";
			else message = Math.floor(nokori / 86400) + "일 전";
		} catch {
			message = timeAgo;
		}
		element.innerHTML = message;
	});
}
timeAgo();

/**
 * 시간입력 제한 함수
 */

function limitStartTime() {
	const inputElements = document.querySelectorAll("[data-max-date]");
	inputElements.forEach(input => {
		const plusDay = input.dataset.maxDate;
		const now = new Date();
		const limitTime = new Date(now);
		limitTime.setSeconds(0);
		const timeString = limitTime.toISOString();
		input.min = timeString.slice(0, timeString.lastIndexOf("."));
	})
}

limitStartTime();