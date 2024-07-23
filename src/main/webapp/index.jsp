<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hello, Web!</title>
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/main.css">
</head>
<body>
<div class="container">
    <header class="header">
        <h1 class="header-title">HELLO. WEB!</h1>
        <nav>
            <%
                Boolean isLogined = (Boolean) session.getAttribute("isLogined");
                String nickname = (String) session.getAttribute("nickname");
                if (isLogined != null && isLogined) {
            %>
            <span class="user-name">환영합니다, <%= nickname %>!</span>
            <form action="logout" method="post" style="display: inline;">
                <button type="submit" class="logout-button">로그아웃</button>
            </form>
            <% } else { %>
            <form action="login" method="get" style="display: inline;">
                <button type="submit" class="login-button">로그인</button>
            </form>
            <form action="signup" method="get" style="display: inline;">
                <button type="submit" class="signup-button">회원가입</button>
            </form>
            <% } %>
        </nav>
    </header>
    <section class="banner">
        <h2 class="banner-title">우아한 테크캠프 백엔드 교육용 페이지</h2>
        <p class="banner-subtitle">HELLO, WEB! 입니다.</p>
    </section>
    <section class="post-list">
        <div class="post-count">전체 글 <%= request.getAttribute("postCount") %>개</div>
        <div class="post-header">
            <div>제목</div>
            <div>작성자</div>
            <div>작성일자</div>
            <div>조회수</div>
        </div>
        <%
            List<Map<String, Object>> posts = (List<Map<String, Object>>) request.getAttribute("posts");
            if (posts != null) {
                for (Map<String, Object> post : posts) {
        %>
        <article class="post-item">
            <div class="post-content">
                <h3><%= post.get("title") %></h3>
                <div class="post-author"><%= post.get("author") %></div>
                <time class="post-date"><%= post.get("date") %></time>
                <div class="post-views"><%= post.get("views") %></div>
            </div>
            <div class="divider"></div>
        </article>
        <%
                }
            }
        %>
    </section>
    <div class="pagination">
        <!-- Pagination component content -->
    </div>
</div>
</body>
</html>
