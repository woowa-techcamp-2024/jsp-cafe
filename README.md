# JSP Cafe
## Package Directory
```
jspcafe
├── web
│   ├── exception
│   ├── filter
│   └── servlet
│       ├── article
│       ├── reply
│       ├── user
│       └── utils
│
├── service
│   └── dto
│
├── domain
│   └── exception
│
├── repository
│   ├── article
│   ├── user
│   ├── reply
│   └── dto
│
├── infra
│   └── time
│
└── configuration
```

<br>

## API
| URI | Method | 기능 | 담당 서블릿 | 인증 |
| --- | --- | --- | --- | --- |
| 회원 |  |  |  |  |
| /users | GET | 회원 목록 조회 | UserServlet | O |
| /users/{userId}/ | GET | 회원 상세 조회 | UserServlet | O |
| /users/edit/{userId} | GET | 회원 정보 수정 폼 조회 | UserEditServlet | O |
| /users/edit/{userId} | POST | 회원 정보 수정 처리 | UserEditServlet | O |
| /users/registration | GET | 회원 가입 폼 조회  | UserRegistrationServlet | X |
| /users/registration | POST | 회원 가입 처리 | UserRegistrationServlet | X |
| /users/login | GET | 로그인 폼 조회 | UserLoginServlet | X |
| /users/login | POST | 로그인 처리 | UserLoginServlet | X |
| /users/logout | POST | 로그아웃 처리 | UserLogoutServlet | O |
|  |  |  |  |  |
| 게시글 |  |  |  |  |
| /articles?page=xxx | GET | 게시글 목록 N개 조회 | ArticleServlet | X |
| /articles/{articleId} | GET | 게시글 상세 조회 | ArticleServlet | O |
| /articles/write | GET | 게시글 작성 폼 조회 | ArticleWriteServlet | O |
| /articles/write | POST | 게시글 작성 | ArticleWriteServlet | O |
| /articles/edit | GET | 게시글 수정 폼 조회 | ArticleEditServlet | O |
| /articles/edit/{articleId} | PUT | 게시글 수정 | ArticleEditServlet | O |
| /articles/edit/{articleId} | DELETE | 게시글 삭제 | ArticleEditServlet | O |
|  |  |  |  |  |
| 댓글 |  |  |  |  |
| /comments | POST | 게시글 댓글 작성 | CommentServlet | O |
| /comments | GET | 게시글 댓글 목록 조회 | CommentServlet | O |
| /comments/{commentId} | DELETE | 댓글 삭제 | CommentServlet | O |
|  |  |  |  |  |

<br>

## 기능 목록
### 회원
#### 회원 가입 기능
```
요구사항

1. 이메일, 닉네임, 비밀번호 정보로 회원가입한다.
2. 회원 가입 시, 현재 날짜를 기준으로 회원 가입 날짜를 기록한다.
3. 회원 가입시 회원 목록 페이지로 리다이렉트한다.
```

#### 로그인 기능
```
요구사항:

1. 현재 상태가 로그인 상태이면, 상단 메뉴에 "멤버리스트", "마이페이지", "로그아웃", "개인정보수정"이 표시
2. 로그인 성공시, 쿠키에 로그인 정보를 포함하여 응답으로 내린다.
3. 로그인 성공시, 세션에 사용자 정보를 저장한다.
```

#### 로그아웃 기능

```
요구사항:

1. 현재 상태가 로그인 상태가 아니면, 상단 메뉴에 "로그인", "회원가입"이 표시
2. 로그아웃 성공시, 쿠키 정보를 초기화하여 응답으로 내린다.
3. 로그아웃 성공 시, 세션에 저장된 사용자 정보를 초기화한다.
```

#### 회원정보 수정 기능
```
요구사항:

1. 회원 목록 페이지에서 회원가입한 사용자의 정보를 수정할 수 있다.
2. 비밀번호를 수정할 수 있다.
3. 닉네임을 수정할 수 있다.
4. 비밀번호가 일치하는 경우에만 수정 가능하다.
5. 다른 회원의 수정 폼을 GET 요청하면, 메인 페이지로 리다이렉트 한다.
6. 다른 회원에 대한 수정하기 POST 요청하면, 메인 페이지로 리다이렉트 한다.
```

#### 개인정보 수정 기능
```
요구사항:

1. 로그인한 사용자가 자신의 개인정보를 수정할 수 있어야 한다.
2. 닉네임, 비밀번호만 수정할 수 있다. 사용자 이메일은 수정할 수 없다.
3. 비밀번호가 일치하는 경우에만 수정할 수 있다.
```

#### 회원 목록 조회 기능
```
요구사항
1. 가입한 회원의 목록을 조회할 수 있어야 한다.
2. 회원 목록의 데이터는 닉네임, 이메일, 가입한 날짜이다.
3. 닉네임을 클릭하면 프로필을 조회한다.
3. 회원 목록은 동적으로 랜더링할 수 있어야 한다.
```

#### 프로필 조회 기능
```
요구사항
1. 특정 사용자의 정보를 조회할 수 있어야 한다.
2. 프로필 데이터는 기본 사진, 닉네임, 이메일이다.
3. 프로필 조회는 동적으로 랜더링할 수 있어야 한다.
```

#### 쿠키 / 세션 기반 인증 필터 기능
```
요구사항:

1. 인증 실패시, 로그인 페이지로 리다이렉트
  - 쿠키가 없는 요청
  - 쿠키가 있는데, 로그인 쿠키가 없는 요청
  - 로그인 쿠키이름과 로그인 세션키가 일치하지 않는 요청
  - 로그인 쿠키값(userId)와 세션값(User)의 userId가 일치하지 않는 요청
```

<br>

### 게시글
#### 게시글 작성 기능
```
요구사항:
1. 로그인한 사용자는 게시글을 작성할 수 있다.
  - 비로그인 사용자가 글쓰기 페이지에 접근할 경우, 로그인 페이지로 리다이렉트 한다.
2. 게시글을 저장할 수 있다.
3. 특정 게시글을 조회할 수 있다.
4. 특정 페이지의 게시글 목록을 10개 단위로 조회할 수 있다. (offset기반 페이지네이션)
  - 게시글 목록은 작성일자 기준 내림차순 정렬한다.
5. 게시글 작성을 완료하면 메인 페이지로 리다이렉트 한다.
6. 게시글 목록의 제목을 클릭하면 게시글 상세 페이지로 이동한다.
```

#### 게시글 수정하기 기능
```
요구사항:

1. 수정하기 폼과 수정하기 기능은 로그인 사용자만 사용 가능하다.
2. 비로그인 사용자일 시, 로그인 페이지로 리다이렉트 한다.
3. 수정하기 폼과 수정하기 기능은 글쓴이 이메일과 같은 경우에만 가능하다.
4. 이메일이 다를 경우, 메인 페이지로 리다이렉트 한다.
5. 게시글 제목과 내용을 수정할 수 있다.
```

#### 게시글 삭제하기 기능
```
요구사항:

1. 삭제하기는 로그인 사용자만 사용 가능하다.
  - 비로그인 사용자일 시, 로그인 페이지로 리다이렉트 한다.
2. 삭제하기는 글쓴이의 이메일과 같은 경우에만 가능하다.
3. 이메일이 다를 경우, 메인 페이지로 리다이렉트 한다.
```

<br>

## 테스트 커버리지
> 비즈니스 요구사항이 담겨있는 Service와 Domain은 일정 수치 이상의 테스트 커버리지를 유지하려 했습니다.

<img width="831" alt="image" src="https://github.com/user-attachments/assets/267c4e08-2aa3-4548-8395-e2488f006d18">

<br>

## Configuration
- Java 17
- Tomcat 10.1.26
- JSP
- MySQL 8  (Production)
- H2 2.2.224 In-Memory DB (Test)