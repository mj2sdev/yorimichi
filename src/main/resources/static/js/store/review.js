/**
 * 리뷰 모달에서 리뷰 이미지(여러개) 입력 시 아래칸에 미리보기를 보여주는 함수
 * changed는 리뷰를 수정할 경우 이미지가 수정되면 수정되었다고 알리기 위한 변수
 * true 라면 서버측에서 multipartFile 리스트를 활용하여 다시 이미지를 등록해야 하고
 * false 라면 multipartFile 관련 로직을 스킵하면 됩니다.
 * 
 * template 은 hidden 속성으로 숨겨진 엘리먼트를 복제하여 해당 엘리먼트에 이미지 미리보기를 첨부하고
 * preview 의 리스트에 추가하여 시각화 합니다. 
 * fileReader 객체는 input[type=file] 요소에 등록(업로드) 되어있는 파일을 꺼내 웹 페이지에서 바로 사용할 수 있는
 * base64형식으로 파일을 재 구성합니다.
 * onload 속성에 콜백 함수를 설명
 * -> base64 형식으로 파일을 읽는데 성공한다면 img 태그의 sc 속성에 해당 파일 링크를 삽입합니다. 
 * 
 * @param {Event} event 
 * @author mj2sdev
 * @version 1.0 초안작성
 */
function previewReviewImages(event) {
	const input = event.target;
	const preview = document.querySelector(".previewReviewImages");
	const template = preview.querySelector(".template").cloneNode(true);
	const changed = document.querySelector("[name=changedReviewImages]");
	
	changed.value = true;

	template.classList.remove("template");
	template.removeAttribute("hidden");

	Array.from(preview.children).filter(child => !child.hasAttribute("hidden"))
		.forEach(element => element.remove());

	Array.from(input.files).forEach(file => {
		const copiedTemplate = template.cloneNode(true);
		const reader = new FileReader();
		reader.onload = fileEvent => copiedTemplate.querySelector("img").setAttribute("src", fileEvent.target.result);
		reader.readAsDataURL(file);
		preview.appendChild(copiedTemplate);
	})
}