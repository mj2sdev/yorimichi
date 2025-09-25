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