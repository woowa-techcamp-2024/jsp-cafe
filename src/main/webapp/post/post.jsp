<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.jspcafe.post.response.PostResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.jspcafe.post.response.CommentResponse" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    PostResponse post = (PostResponse) request.getAttribute("post");
    List<CommentResponse> comments = (List<CommentResponse>) request.getAttribute("comments");

    // 출력 형식 지정
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
    String formattedDate = (post.createdAt() != null) ? post.createdAt().format(formatter) : "날짜 형식 오류";

    Boolean isLogined = (Boolean) session.getAttribute("isLogined");
    Long loggedInUserId = (Long) session.getAttribute("userId"); // 로그인한 사용자의 ID
    Long postAuthorId = post.userId(); // 글쓴이의 사용자 ID
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= post.title() %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/post.css">
</head>
<body>
<div class="container">
    <header class="header">
        <h1 class="header-title"><a href="/">HELLO, WEB!</a></h1>
        <nav>
            <%
                if (isLogined != null && isLogined) {
                    String nickname = (String) session.getAttribute("nickname");
            %>
            <span class="user-name">환영합니다, <%= nickname %>!</span>
            <form action="${pageContext.request.contextPath}/api/logout" method="post" style="display: inline;">
                <button type="submit" class="logout-button">로그아웃</button>
            </form>
            <form action="${pageContext.request.contextPath}/edit-profile.jsp" method="get" style="display: inline;">
                <button type="submit" class="edit-profile-button">정보수정</button>
            </form>
            <% } else { %>
            <form action="${pageContext.request.contextPath}/login" method="get" style="display: inline;">
                <button type="submit" class="login-button">로그인</button>
            </form>
            <form action="${pageContext.request.contextPath}/signup" method="get" style="display: inline;">
                <button type="submit" class="signup-button">회원가입</button>
            </form>
            <% } %>
            <form action="${pageContext.request.contextPath}/users" method="get" style="display: inline;">
                <button type="submit" class="user-list-button">사용자 목록</button>
            </form>
        </nav>
    </header>
    <main class="main-content">
        <div class="article-container">
            <header class="article-header">
                <h2 class="article-title"><%= post.title() %></h2>
                <div class="article-meta">
                    <span class="article-author">작성자: <%= post.nickname() %></span>
                    <span class="article-date">작성일자: <%= formattedDate %></span>
                    <span class="article-views">조회수: 1</span>
                        <%
                            if (isLogined != null && isLogined && loggedInUserId != null && loggedInUserId.equals(postAuthorId)) {
                        %>
                        <form action="${pageContext.request.contextPath}/post/edit/<%= post.postId() %>" method="get" style="display: inline;">
                            <button type="submit" class="edit-post-button active">수정</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/api/delete-post/<%= post.postId() %>" method="post" style="display: inline;">
                            <button type="submit" class="delete-post-button active">삭제</button>
                        </form>
                        <% } %>
                </div>
            </header>
            <article class="article-content">
                <%= post.content() %>
            </article>
            <section class="comment-section">
                <div class="comment-count">댓글 <%= comments.size() %>개</div>
                <%
                    for (CommentResponse comment : comments) {
                %>
                <div class="comment">
                    <div class="comment-author"><%= comment.nickname() %></div>
                    <div class="comment-content"><%= comment.content() %></div>
                    <div class="comment-date"><%= (comment.createdAt() != null) ? comment.createdAt().format(formatter) : "날짜 형식 오류" %></div>
                </div>
                <%
                    }
                %>
            </section>
            <%
                String commentWriter = (String) request.getAttribute("commentWriter");
            %>
            <form action="${pageContext.request.contextPath}/api/comments" method="post" class="comment-form">
                <label for="commentInput" class="comment-form-label"><%= commentWriter %></label>
                <textarea id="commentInput" name="content" class="comment-form-textarea" placeholder="댓글 입력"></textarea>
                <button type="submit" class="comment-form-button">댓글입력</button>
            </form>
            <nav class="navigation-buttons">
                <form action="/index.jsp" method="get">
                    <button type="submit" class="navigation-button">목록으로</button>
                </form>
            </nav>
        </div>
    </main>
</div>
<!-- 에러 메시지 모듈 포함 -->
<%
    String errorMessage = (String) request.getAttribute("errorMessage");
%>
<jsp:include page="/WEB-INF/jsp/common/error-message.jsp">
    <jsp:param name="errorMessage" value="<%= errorMessage %>" />
</jsp:include>
</body>
</html>
