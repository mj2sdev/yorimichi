# endpoint 정리 문서

엔드포인트(endpoint)란, 외부에서 시스템이나 서비스에 접근할 수 있는 최종 지점을 의미합니다.  
일반적으로 웹 API에서는 특정 기능이나 데이터를 제공하는 URL 주소(`/api/users`, `/api/login` 등)가 엔드포인트가 됩니다.  

즉, 클라이언트가 서버로 요청을 보낼 때 도착하는 경로를 엔드포인트라고 하며, 각 엔드포인트는 고유한 역할(예: 로그인 처리, 회원 정보 조회 등)을 담당합니다.  

정리하면, 엔드포인트는 **특정 기능을 호출하기 위한 접근 경로(URL, URI 등)** 라고 말씀드릴 수 있습니다.

형식
index.html METHOD `URL` |요구 데이터| : 설명

## header.html
header.html GET `/index` || : 로고 클릭시 index.html로 이동
header.html GET `/station` || : 정류장 아이콘 클릭시 station.html로 이동
header.html GET `/login` || : 로그인 아이콘 클릭시 login.html로 이동
header.html GET `/search `|| : 검색창에 검색시 store/list.html로 이동
header.html GET `/mypage` || : 마이페이지 버튼 클릭시 사용자의 페이지로 이동
header.html GET `/logout` || : 버튼 클릭시 사용자 로그아웃.
header.html GET `/like` |like 가게 리스트| : store/list.html로 이동 및, 유저의 좋아요 가게 리스트를 우선으로 보여줌.
header.html POST `/notification` |알림리스트| : header의 알림리스트에 목록을 띄움.
header.html GET `/notification/review/{id}` |id에 해당하는 리뷰 정보| : 리뷰 정보를 가져와서 리뷰 모달에 띄움
header.html GET `/notification/coeat/{id}` |id에 해당하는 coeat 정보| : 같이먹기 정보를 가져와서 같이먹기 모달에 띄움
header.html GET `/coeat/participate/{id}` || : 같이먹기 신청자 명단에 등록하고, 참가요청 버튼이 신청 버튼으로 바뀜.

