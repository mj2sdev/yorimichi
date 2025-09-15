# modal 이란
누르면 화면 최상단레이어에 새로운 창을 띄워 무언가를 보여주는 방식.

## 어떤 속성을 주목해서 봐야하는가.
data-bs-toggle="modal" 얘로 뭘 열고 닫을 건지 설정
data-bs-target="#id" 정확히 어떤 것을 열고 닫을 건지 설정.
data-bs-dismiss="modal" 얘로 뭘 끌 건지 설정

## 어떻게 설계할 것인가.

templates/fragments/review/modal.html을 정의함.
이 페이지에 실제로 어떠한 값이 들어가는 게 아니라
다른 곳에서 리뷰모달을 불러올 때 사용할 수 있도록 하는 틀을 설정.

templates/script/review/modal.js를 정의함
이것은 리뷰 모달 버튼을 누르면 비동기식으로 내용을 가져와서
replace로 대체된 리뷰 모달 자리에 값을 집어넣는 작업을 함.