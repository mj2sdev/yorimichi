# endpoint 정리 문서

엔드포인트(endpoint)란, 외부에서 시스템이나 서비스에 접근할 수 있는 최종 지점을 의미합니다.  
일반적으로 웹 API에서는 특정 기능이나 데이터를 제공하는 URL 주소(`/api/users`, `/api/login` 등)가 엔드포인트가 됩니다.  

즉, 클라이언트가 서버로 요청을 보낼 때 도착하는 경로를 엔드포인트라고 하며, 각 엔드포인트는 고유한 역할(예: 로그인 처리, 회원 정보 조회 등)을 담당합니다.  

정리하면, 엔드포인트는 **특정 기능을 호출하기 위한 접근 경로(URL, URI 등)** 라고 말씀드릴 수 있습니다.

형식
기능이 존재하는.html METHOD `URL` |요구 데이터| :: 기능명 - 설명 // header.html  
요구 데이터란 서버로부터 사용자에게 보내줘야 하는 데이터를 의미한다.


## AuthController
-   POST `/logout` |null| :: 로그아웃 - 버튼 클릭시 사용자 로그아웃. // header.html
-   GET `/signup` |null| :: 이동 - signup.html로 이동. // login.html
-   POST `/signup` |?| :: 회원가입 - 유효성 검사 및 실패시 사유 표시. // signup.html
-   POST `/signup/social` |?| :: 소셜 회원가입
-   GET `/signup/nickname` |중복여부boolean| :: 닉네임 - 중복여부 비동기 // signup.html
-   POST `/signup/eamil` |검사확인여부boolean| :: 이메일확인 - 누르면 이메일 중복 검사? 혹은 이메일 확인? 용 이메일 전송. // signup.html
-   GET `/login` |null| :: user/login.html로 이동
-   POST `/login` |실패시 사유| :: 로그인 - 로그인 처리용. 유효성 검사 필요. // login.html
-   POST `/login/social` |?| :: 소셜로그인 - 소셜로그인 처리용 // login.html

## CoeatController
-   GET `/coeats` |List<CoeatDTO>| :: 최신 같이먹기 리스트를 불러옴.
-   GET `/coeats/{id}` |List<CoeatDTO>| :: 특정 가게의 같이먹기 리스트를 불러옴 
-   GET `/coeat/{coeatid}` |CoeatDTO| ::  알림코잇 - 같이먹기 정보를 가져와서 같이먹기 모달에 띄움 // header.html
-   POST `/coeat` |List<CoeatDTO>| :: 같이먹기작성 - 작성 // store/detail.html
-   PUT `/coeat/{id}` || :: 같이먹기수정 - 수정 // store/detail.html
-   DELETE `/coeat/{id}` |List<CoeatDTO>| :: 같이먹기삭제 - 삭제 // store/detail.html
-   POST `/coeat/{coeatId}/participant` |List<CoeatRequestDTO>| :: 코잇참가 - 같이먹기 요청자 명단에 등록하고, 참가요청 버튼이 요청완료 버튼으로 바뀜. // header.html
-   PATCH `/coeat/{coeatId}/participant/{participantId}` |List<CoeatRequestDTO>| :: 같이먹기참가 수락 또는 거절 - 참가자 명단에서 참가자로 변경. // store/detail.html
-   DELETE `/coeat/{coeatid}/participant` |List<CoeatRequestDTO>| :: 같이먹기참가취소 - 참가자 명단에서 제거 // store/detail.html

## NotificationController
-   GET `/notification` |List<NotificationDTO>| :: 알림버튼 - header의 알림리스트에 목록을 띄움. // header.html
-   PATCH `/notificaton/{id}` |null| :: 알림을 읽으면 그 알림을 읽음처리 합니다.

## PageController
-   GET `/index` |List<StoreDTO>|List<CategoryDTO>| :: 로고 - 로고 클릭시 index.html로 이동 // header.html
> PageController는 점점 존재의의가 희미해져서 차후에 /index를 다른 곳에 편입시키고 없애고자 합니다. 단순 사이트 내에 페이지 이동 관련 메서드도 PageController가 아닌 그 페이지와 밀접한 기능이 모여있는 controller로 이동시켜주기시길 바랍니다.

## RelationshipController
-   GET `/follow` |List<UserDTO>| :: 사용자가 팔로우한 사람 리스트
-   GET `/follower` |List<UserDTO>| :: 사용자를 팔로우한 사람 리스트
-   POST `/follow/{targetId}` || :: 유저팔로우 - 유저를 팔로우 목록에 넣음. // store/detail.html
-   DELETE `/follow/{targetId}` || :: 유저와의 관계를 삭제함. // store/detail.html
-   GET `/block` |List<UserDTO>| :: 사용자가 차단한 사람 리스트
-   POST `/block/{targetId}` || :: 유저차단 - 유저차단목록에넣음 // mypage.html
-   DELETE `/block/{targetId}` || :: 유저차단해제 - 차단목록에서 제거. // mypage.html

## ReportController
-   POST `/report/{category}/{rootId}` |null| :: 신고 - 신고해서 신고 대상/ 카테고리/ 내용을 리스트에 전달. // store/detail.html
-   Put `/report/(reportId)` || :: 접수된 신고 수락 및 조치.
-   DELETE `/report/{reportId}`|| :: 접수된 신고 반려.

## ReviewController
-   GET `/reviews` |List<ReviewDTO>| :: 가게와 상관 없이 최신 리뷰 리스트를 불러옴.
-   GET `/review/{id}` |ReviewDTO| :: 알림리뷰 - 리뷰 정보를 가져와서 리뷰 모달에 띄움 // header.html
-   POST `/review` |List<ReviewDTO>| :: 리뷰작성 - 작성 // store/detail.html
-   PUT `/review` || :: 리뷰수정 - 수정 // store/detail.html
-   DELETE `/review` |List<ReviewDTO>| :: 리뷰삭제 - 삭제 // store/detail.html

## StationController
- GET `/station` || :: 정류장으로 이동
- GET `/feed/{count}` |List<FeedDTO>| :: 리뷰와 같이먹기의 목록을 가져오는 용도.

## StoreController
-   GET `/search` |List<StoreDTO>| :: 가게 리스트를 비동기로 불러오기 위함. index 페이지에서 맛집 리스트를 요구하기 때문에, 키워드나 태그 없이 가져오도록 함.
-   GET `/search?keyword={keyword}&tags={tags}` |List<StoreDTO>|List<StoreCategoryDTO>|List<StoreFacilityCategoryDTO>:: 검색 - 동기식 리스트 페이지로 이동.
-   GET `/search/async?keyword={keyword}&tags={tags}`|List<StoreDTO>| :: 필터 - 비동기식 리스트만 갱신 // header.html
-   GET `/store/detail` |StoreDTO|:: 가게 상세 - 클릭시 특정 가게의 상세페이지로 감. // index.html
-   POST `/store/bookmark` |List<BookmarkDTO>| :: 즐겨찾기 - 가게를 즐겨찾기 목록에 등록함. // store/detail.html
-   DELETE `/store/bookmark` |List<BookmarkDTO>| :: 즐겨찾기해제 - 가게를 즐겨찾기 목록에서 제거함. // store/detail.html
-   GET `/store/like` |List<LikeDTO>| :: 좋아요 - store/list.html로 이동 및, 유저의 좋아요 가게 리스트를 우선으로 보여줌. // header.html
-   POST `/store/like` |List<LikeDTO>| :: 즐겨찾기 - 가게를 좋아요 목록에 등록함. // store/detail.html
-   DELETE `/store/like` |List<LikeDTO>| :: 즐겨찾기해제 - 가게를 좋아요 목록에서 제거함. // store/detail.html

## UserController
-   GET `/user/detail/{id}` || :: 유저정보 - 유저의 detail을 알아옴. // store/detail.html
-   GET `/user/mypage` || :: 마이페이지 - 마이페이지 버튼 클릭시 사용자의 페이지로 이동 // header.html
-   PUT `/user/mypage` || :: 개인정보수정 - 닉네임이나 이메일 등 개인정보 수정 // mypage.html
-   PUT `/user/mypage/privacy` || :: 개인정보 공개처리 - 버튼 딸깍으로 개인정보를 공개할지 말지를 결정함. 저장시 저장해서 db 갱신 // mypage.html