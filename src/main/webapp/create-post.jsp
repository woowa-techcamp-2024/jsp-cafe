<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 작성</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/header.css">
    <link rel="stylesheet" href="../css/create-post.css">
</head>
<body>
<%
    Boolean isLogined = (Boolean) session.getAttribute("isLogined");
    if (isLogined == null || !isLogined) {
        response.sendRedirect("/login"); // 로그인 페이지로 리디렉션
        return;
    }
%>
<div class="container">
    <header class="header">
        <h1 class="header-title"><a href="/">HELLO, WEB!</a></h1>
        <nav>
            <form action="../logout" method="post" style="display: inline;">
                <button type="submit" class="logout-button">로그아웃</button>
            </form>
            <form action="../profile" method="get" style="display: inline;">
                <button type="submit" class="profile-button">마이페이지</button>
            </form>
            <form action="../users" method="get" style="display: inline;">
                <button type="submit" class="user-list-button">멤버리스트</button>
            </form>
        </nav>
    </header>
    <main class="main-content">
        <div class="post-container">
            <h1 class="post-title">글쓰기</h1>
            <form class="post-form" action="/api/posts" method="post">
                <div class="input-field">
                    <label for="title">제목</label>
                    <input
                            type="text"
                            id="title"
                            name="title"
                            placeholder="글의 제목을 입력하세요"
                            required
                    />
                </div>
                <div class="input-field">
                    <label for="content">내용</label>
                    <textarea
                            id="content"
                            name="content"
                            placeholder="글의 내용을 입력하세요"
                            required
                    ></textarea>
                </div>
                <button type="submit" class="submit-button">작성 완료</button>
            </form>
        </div>
    </main>
</div>
</body>
</html>
