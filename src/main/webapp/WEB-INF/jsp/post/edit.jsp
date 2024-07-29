<%@ page import="org.example.jspcafe.post.model.Post" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 수정</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create-post.css">
</head>
<body>
<%
    Boolean isLogined = (Boolean) session.getAttribute("isLogined");
    if (isLogined == null || !isLogined) {
        response.sendRedirect("${pageContext.request.contextPath}/login");
        return;
    }
    String errorMessage = (String) request.getAttribute("errorMessage");
    Post post = (Post) request.getAttribute("post");
%>
<div class="container">
    <header class="header">
        <h1 class="header-title"><a href="${pageContext.request.contextPath}/">HELLO, WEB!</a></h1>
        <nav>
            <form action="${pageContext.request.contextPath}/logout" method="post" style="display: inline;">
                <button type="submit" class="logout-button">로그아웃</button>
            </form>
            <form action="${pageContext.request.contextPath}/profile" method="get" style="display: inline;">
                <button type="submit" class="profile-button">마이페이지</button>
            </form>
            <form action="${pageContext.request.contextPath}/users" method="get" style="display: inline;">
                <button type="submit" class="user-list-button">멤버리스트</button>
            </form>
        </nav>
    </header>
    <main class="main-content">
        <div class="post-container">
            <h1 class="post-title">게시글 수정</h1>
            <form class="post-form" action="${pageContext.request.contextPath}/api/posts/<%= post.getPostId() %>" method="post">
                <div class="input-field">
                    <label for="title">제목</label>
                    <input
                            type="text"
                            id="title"
                            name="title"
                            placeholder="글의 제목을 입력하세요"
                            value="<%= post.getTitle().getValue() %>"
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
                    ><%= post.getContent().getValue() %></textarea>
                </div>
                <button type="submit" class="submit-button">수정 완료</button>
            </form>

            <!-- 에러 메시지 모듈 포함 -->
            <jsp:include page="/WEB-INF/jsp/common/error-message.jsp">
                <jsp:param name="errorMessage" value="<%= errorMessage %>" />
            </jsp:include>
        </div>
    </main>
</div>
</body>
</html>
