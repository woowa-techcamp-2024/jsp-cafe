<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hello, Web!</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<div class="container">
    <header class="header">
        <h1 class="header-title"><a href="/">HELLO, WEB!</a></h1>
        <nav>
            <%
                Boolean isLogined = (Boolean) session.getAttribute("isLogined");
                String nickname = (String) session.getAttribute("nickname");
                if (isLogined != null && isLogined) {
            %>
            <span class="user-name">환영합니다, <%= nickname %>!</span>
            <form action="api/logout" method="post" style="display: inline;">
                <button type="submit" class="logout-button">로그아웃</button>
            </form>
            <form action="edit-profile.jsp" method="get" style="display: inline;">
                <button type="submit" class="edit-profile-button">정보수정</button>
            </form>
            <% } else { %>
            <form action="login" method="get" style="display: inline;">
                <button type="submit" class="login-button">로그인</button>
            </form>
            <form action="signup" method="get" style="display: inline;">
                <button type="submit" class="signup-button">회원가입</button>
            </form>
            <% } %>
            <form action="${pageContext.request.contextPath}/users" method="get" style="display: inline;">
                <button type="submit" class="user-list-button">사용자 목록</button>
            </form>
        </nav>
    </header>
    <section class="banner">
        <h2 class="banner-title">우아한 테크캠프 백엔드 교육용 페이지</h2>
        <p class="banner-subtitle">HELLO, WEB! 입니다.</p>
    </section>
    <section class="post-list">
        <jsp:include page="api/posts" />
    </section>
    <div class="pagination">
        <!-- Pagination component content -->
    </div>
    <div class="write-post">
        <form action="create-post.jsp" method="get">
            <button type="submit" class="write-post-button">글쓰기</button>
        </form>
    </div>
</div>
</body>
</html>