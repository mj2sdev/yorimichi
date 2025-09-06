# Thymeleaf를 이용한 레이아웃 구성법

## `th` 속성이 오류를 일으키지 않는 이유

1.  **HTML의 기본 동작**: HTML은 표준이 아닌 태그나 속성을 만나면 오류를 발생시키는 대신 기본적으로 **무시**합니다. 따라서 `th:`와 같은 비표준 속성이 있어도 웹 브라우저는 문제없이 페이지를 렌더링합니다.

2.  **XML 네임스페이스(namespace) 선언**: `<html>` 태그에 `xmlns:th="[http://www.thymeleaf.org](http://www.thymeleaf.org)"`를 선언하면, 이 문서 내에서 `th:` 접두사를 가진 모든 속성은 Thymeleaf의 문법임을 명시하게 됩니다. 이를 통해 Thymeleaf 엔진이 해당 속성들을 인식하고 처리할 수 있게 됩니다.

---

## `th:fragment` 사용법

재사용 가능한 HTML 조각을 정의할 때 사용합니다.

* `th:fragment="이름"`: 특정 태그 블록에 고유한 이름을 부여합니다.
* `th:fragment="이름 (매개변수1, 매개변수2, ...)"`: 프래그먼트에 이름을 부여하고, 외부에서 매개변수를 받아 내용을 동적으로 변경할 수 있도록 정의합니다.

---

## `th:replace` 사용법

`th:fragment`로 정의된 조각을 가져와 현재 태그를 완전히 대체합니다.

* `th:replace="~{폴더명/파일명 :: 프래그먼트이름}"`
    * 지정된 경로의 HTML 파일에서 `프래그먼트이름`을 가진 조각을 가져와 삽입합니다.

* `th:replace="~{폴더명/파일명 :: 프래그먼트이름 (매개변수1=값1, 매개변수2=값2)}"`
    * 프래그먼트를 가져오면서 필요한 매개변수를 전달합니다.
    * 매개변수 값으로는 다른 프래그먼트(`~{::다른프래그먼트이름}`)를 넘길 수도 있습니다.
    * `::` 앞에 경로가 없으면 현재 파일 내의 프래그먼트를 참조합니다.
    * **익명 프래그먼트**: 명시적인 `th:fragment`가 없어도, `<매개변수명></매개변수명>`과 같이 태그 자체를 익명 조각으로 간주하여 전달할 수 있습니다.

---

## `<th:block>` 태그

* `<th:block></th:block>`
    * HTML의 `<div>`처럼 영역을 묶는 역할을 하지만, 렌더링 후에는 **태그 자체가 사라지고** 내부 콘텐츠만 남습니다. 불필요한 태그 없이 Thymeleaf 로직을 적용하고 싶을 때 유용합니다.

---

## `th:replace`의 정적/동적 참조

* **정적 참조**: `th:replace="~{...}"`
    * 템플릿 경로와 프래그먼트 이름을 직접 명시하는 방식으로, 항상 동일한 조각을 가져옵니다.

* **동적 참조**: `th:replace="${변수명}"`
    * 컨트롤러 등에서 모델에 담아 전달한 변수값을 참조합니다. 변수값에 따라 가져오는 프래그먼트가 달라질 수 있습니다.

---

## 파싱 에러 방지

페이지에 따라 전달되는 매개변수가 없을 경우 발생하는 오류를 방지할 수 있습니다.

* `<th:block th:replace="${매개변수명} ?: ~{}"></th:block>`
    * **Elvis 연산자(`?:`)**를 사용합니다.
    * `${매개변수명}`이 존재하고 `null`이 아니면 해당 값을 사용하고, 그렇지 않으면 `~{}` (내용이 없는 빈 조각)으로 대체하여 오류를 막습니다.

---

## 작성 예시
* [layout.html](https://github.com/mj2sdev/yorimichi/blob/front/src/main/resources/templates/layout/layout.html)
* [header.html](https://github.com/mj2sdev/yorimichi/blob/front/src/main/resources/templates/layout/header.html)
* [index.html](https://github.com/mj2sdev/yorimichi/blob/front/src/main/resources/templates/index.html)