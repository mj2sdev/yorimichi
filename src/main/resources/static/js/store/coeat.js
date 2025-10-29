/// <reference path="../request.js"/>
/**
 * Coeat 작성 함수
 * @author mj2sdev
 * @version 1.0 초안작성
 * @param {Event} event 
 */
async function writeCoeat(event) {
	event.preventDefault();
	const { target } = event;
	const formData = new FormData(target);
	const body = Object.fromEntries(formData.entries());
	const result = await request("POST", "/coeat", { body });

	if (result === true) {
		alert("같이먹기가 작성되었습니다.");
	} else {
		alert("같이먹기 작성 중 오류가 발생하였습니다.");
	}
	// location.reload();
}