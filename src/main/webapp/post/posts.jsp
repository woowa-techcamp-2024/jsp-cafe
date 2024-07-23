<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.jspcafe.post.response.PostListResponse" %>
<%@ page import="org.example.jspcafe.post.response.PostResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/post-list.css">
<%
    PostListResponse postListResponse = (PostListResponse) request.getAttribute("posts");
    int postCount = postListResponse != null ? postListResponse.totalElements() : 0;
    List<PostResponse> posts = postListResponse != null ? postListResponse.postList() : null;

    // 출력 형식 지정
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");

    // 페이지네이션 관련 변수
    int currentPage = request.getAttribute("currentPage") != null ? (int) request.getAttribute("currentPage") : 1;
    int totalPages = request.getAttribute("totalPages") != null ? (int) request.getAttribute("totalPages") : 1;
    int pageSize = request.getAttribute("pageSize") != null ? (int) request.getAttribute("pageSize") : 10;
%>
<div class="post-list">
    <div class="post-count">전체 글 <%= postCount %>개</div>
    <div class="post-header">
        <div class="post-header-title">제목</div>
        <div class="post-header-author">작성자</div>
        <div class="post-header-date">작성일자</div>
    </div>
    <%
        if (posts != null && !posts.isEmpty()) {
            for (PostResponse post : posts) {
                LocalDateTime createdAt = post.createdAt();
                String formattedDate = (createdAt != null) ? createdAt.format(formatter) : "날짜 형식 오류";
    %>
    <article class="post-item">
        <div class="post-content">
            <div class="post-title"><a href="posts/<%= post.postId() %>" class="post-link"><%= post.title() %></a></div>
            <div class="post-author"><a href="users/<%= post.nickname() %>" class="profile-link"><%= post.nickname() %></a></div>
            <div class="post-date"><time class="post-date"><%= formattedDate %></time></div>
        </div>
        <div class="divider"></div>
    </article>
    <%
        }
    } else {
    %>
    <div class="no-posts">게시글이 없습니다.</div>
    <%
        }
    %>
</div>
<%
    if (totalPages > 1) {
%>
<div class="pagination">
    <%
        for (int i = 1; i <= totalPages; i++) {
            if (i == currentPage) {
    %>
    <span><strong><%= i %></strong></span>
    <%
    } else {
    %>
    <a href="?page=<%= i %>&size=<%= pageSize %>"><%= i %></a>
    <%
            }
        }
    %>
</div>
<%
    }
%>
