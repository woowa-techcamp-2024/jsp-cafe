<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hello, Web!</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/header.css">
    <link rel="stylesheet" href="../css/main.css">
</head>
<body>
<div class="container">
    <header class="header">
        <h1 class="header-title">HELLO. WEB!</h1>
        <nav>
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <span class="user-name">${sessionScope.user.name}</span>
                    <form action="logout" method="post" style="display: inline;">
                        <button type="submit" class="logout-button">로그아웃</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <form action="login" method="get" style="display: inline;">
                        <button type="submit" class="login-button">로그인</button>
                    </form>
                    <form action="signup" method="get" style="display: inline;">
                        <button type="submit" class="signup-button">회원가입</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </nav>
    </header>
    <section class="banner">
        <h2 class="banner-title">우아한 테크캠프 백엔드 교육용 페이지</h2>
        <p class="banner-subtitle">HELLO, WEB! 입니다.</p>
    </section>
    <section class="post-list">
        <div class="post-count">전체 글 ${postCount}개</div>
        <div class="post-header">
            <div>제목</div>
            <div>작성자</div>
            <div>작성일자</div>
            <div>조회수</div>
        </div>
        <c:forEach var="post" items="${posts}">
            <article class="post-item">
                <div class="post-content">
                    <h3>${post.title}</h3>
                    <div class="post-author">${post.author}</div>
                    <time class="post-date">${post.date}</time>
                    <div class="post-views">${post.views}</div>
                </div>
                <div class="divider"></div>
            </article>
        </c:forEach>
    </section>
    <div class="pagination">
        <!-- Pagination component content -->
    </div>
</div>
</body>
</html>
