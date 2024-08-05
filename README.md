# 배포 url

- http://3.38.95.195:8080/

# 1 회원 가입 및 목록 조회

- 라이브러리
    - java17, 톰캣10.x, 서블릿, 타임리프

- 웹 페이지
    - 디자인 기획서 참고
- 회원가입 페이지
    - GET /users/register
    - /static/user/form.html
- 회원 목록 조회 페이지
    - GET /users
    - /static/user/list.html
- 프로필 조회 페이지
    - GET /users/{userId}
    - /static/users/profile.html

- 회원 가입 api
    - POST /api/users
    - 완료후 회원 목록 조회 페이지 로 redirect
- 사용자 목록 조회 api
    - GET /api/users
    - 가입한 사용자 목록 조회
- 프로필 조회 api
    - GET /users/{userId}

# 2

- 질문 하기 페이지
    - GET /qna
    - static/qna/form.html
- 질문 목록 조회 페이지
    - GET /
- 회원 정보 수정 페이지
    - GET /users/{userId}/form
    - static/users/updateForm.html
    - 비밀번호, 이름, 이메일 수정 가능

- 질문 하기 api
    - POST /api/questions
    - 성공시 redirect "/"
- 질문 목록 조회 api
    - GET /api/questions
- 회원 정보 수정 api
    - POST /api/users/{userId}/update

# 3

- MySQL 8.x, JDBC
- DB에 사용자, 게시글 저장
- EC2 배포

# 4

- 로그인 중
    - 로그 아웃, 개인 정보 수정 버튼 표시
- 로그인 중이 아닐 때
    - 로그인, 회원가입 버튼 표시
- 로그인 api
    - POST /auth/login
- 로그아웃 api
    - POST /auth/logout

# 5

- 로그인 하지 않은 사용자도 게시글 목록만 조회 가능
- 로그인한 사용자만 다음에 동작 가능, 로그인 하지 않고 접속시 로그인 페이지로 리다이렉트
    - 게시글 상세 보기
    - 게시글 작성
- 로그인시 자신의 글에 대해서만
    - 글 수정 put, 글 삭제 delete
    - 자신의 글이 아닐 때 클릭시 에러 페이지로 이동
    - 삭제의 경우 상태를 삭제로만 변경
    - 댓글이 없는 글만 삭제 가능

# 6

- 로그인한 사용자는 댓글 추가 가능
- 자신이 작성한 댓글은 삭제 가능
    - 삭제 상태로 변경

# 7

- 댓글 관련 요청을 ajax 로 전송

# 8

- 더미 데이터
    - 사용자 10_000
    - 게시글 500_000
    - 게시글 당 댓글 0 ~ 10 랜덤, 평균 5
- 페이지 네이션
    - 페이지당 15개 게시글, 생성일 기준 내림 차순, 네비게이션 버튼 구현
        - 한 네비게이션 안 페이지 버튼은 5개
    - 게시글에 댓글을 5개씩, 내림차순으로 보여준다, 더보기 버튼 구현
- git action 자동 배포