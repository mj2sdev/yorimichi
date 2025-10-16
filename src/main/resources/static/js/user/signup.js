/**
 * 동의관련 내용 토글러
 * @author mj2sdev
 * @version 1.0
 * @param {number} type 
 */
function agreement(type) {
	const agreementModal = document.querySelector("#agreement");
	const agreements = agreementModal.querySelectorAll("[class^='agreement']");
	const titles = agreementModal.querySelectorAll("[class^='title'");
	agreements.forEach(element => element.setAttribute("hidden", ""));
	titles.forEach(element => element.setAttribute("hidden", ""))
	type = type % agreements.length;
	agreements[type].removeAttribute("hidden");
	titles[type].removeAttribute("hidden");
}

/**
 * @author nj2sdev
 * @version 1.0
 * @param {InputEvent} event 
 * 약관동의 모두 체크관련 함수입니다.
 * 1. 체크올이 변동한다 -> 모두 따라한다.
 * 2. 체크스들이 모두 켜지거나 꺼진다 -> 체크올이 따라한다.
 */
function changeAll(event) {
	const ul = event.currentTarget;
	const { target } = event;
	const checkboxAll = ul.querySelector("#all-agree");
	const checkboxs = ul.querySelectorAll("[id*=agree-]");

	if (target.id && target.id.startsWith("all")) {
		return checkboxs.forEach(checkbox => checkbox.checked = target.checked);
	}

	const checkedCount = Array
		.from(checkboxs)
		.map(checkbox => checkbox.checked ? 1 : 0)
		.reduce((acc, cur) => acc + cur, 0);

	if (checkedCount == 3 || checkedCount == 0) 
		checkboxAll.checked = checkboxs[0].checked;
}

/**
 * @author mj2sdev
 * @version 1.0
 * @param {InputEvent} event 
 * 패스워드 인풋 변경이 감지되면 실행되는 함수
 * 해야하는 동작
 * 1. 비밀번호 확인 pattern 설정
 * 2. 비밀번호 보안 컨츄로ㅡ루
 */
function changePassword(event) {
	const { currentTarget } = event;
	const { value } = currentTarget;
	const repassword = document.querySelector("#repassword");
	const passwordLevel = document.querySelector("#password-level");
	const passwordLevelDescription = document.querySelector("#password-level-description");
	const levelBars = passwordLevel.querySelectorAll("span");
	const descriptions = ["없음", "약함", "보통", "강함"];
	const levelColor = ["bg-danger", "bg-warning", "bg-success"];
	const codeRegexp = /[^a-zA-Z0-9]/;

	// 비밀번호 확인 패턴 설정
	if (repassword && value) {
		repassword.setAttribute("pattern", value);
	}

	/**
	 * 레벨 설정
	 * 1: 1글자 이상
	 * 2: 8글자 이상
	 * 3: 특수문자 포함여부
	 * 4: 대문자 포함 여부
	 */
	let level = 0;
	if (value.length) level++;
	if (value.length > 8) level++;
	if (codeRegexp.test(value)) level++;
	
	// 색상 모두 제거
	levelBars.forEach(bar => {
		bar.classList.forEach(cls => {
			if (cls.startsWith("bg-"))
				bar.classList.remove(cls);
		})
	})

	// 색상 칠하기, 설명 추가
	passwordLevelDescription.innerHTML = descriptions[level];
	for (let index = 0; index < level; index++) {
		levelBars[index].classList.add(levelColor[level - 1]);
	}
}

/**
 * 이메일 중복검사 함수입니다.
 * 
 * @author mj2sdev
 * @version 1.0 초안작성
 * @version 1.1 이메일 중복검사 가능하도록 수정
 * 
 */
async function emailDeduplication(event) {
	const input = document.querySelector("#email");
	const password = document.querySelector("#password");
	const email = input.value;

	const method = "POST";
	const url = "/signup/email/verification/{email}";
	const params = { email };
	const usage = await request(method, url, { params });

	console.log(usage, email);
	if (usage) {
		input.setAttribute("pattern", email);
		alert("사용 가능한 이메일 입니다.");
		password.focus();
	} else {
		input.setAttribute("pattern", "");
		alert("이미 사용중인 이메일 입니다.");
		input.focus();
	}
}

/**
 * 닉네임 중복검사 함수입니다.
 * 
 * @author mj2sdev
 * @version 1.0 초안 작성
 * @version 1.1 닉네임 중복검사 가능하도록 수정
 */
async function nicknameDeduplication(event) {
	const input = event.target;
	const nickname = input.value;
	const condition = document.querySelector(".nickname > .condition");
	const duplicate = document.querySelector(".nickname > .duplicate");

	const method = "GET";
	const url = "/signup/nickname";
	const query = { nickname };
	const usage = await request(method, url, { query });
	
	if (!usage) {
		condition.setAttribute("hidden", true);
		duplicate.removeAttribute("hidden");
		input.setAttribute("pattern", "");
	} else {
		condition.removeAttribute("hidden");
		duplicate.setAttribute("hidden", "");
		input.setAttribute("pattern", nickname)
	}

}