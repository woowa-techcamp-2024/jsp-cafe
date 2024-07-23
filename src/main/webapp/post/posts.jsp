<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.jspcafe.post.response.PostListResponse" %>
<%@ page import="org.example.jspcafe.post.response.PostResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/post-list.css">
<%
    PostListResponse postListResponse = (PostListResponse) request.getAttribute("posts");
    int postCount = 0;
    List<PostResponse> posts = null;
    if (postListResponse != null) {
        postCount = postListResponse.totalElements();
        posts = postListResponse.postList();
    }
    // 출력 형식 지정
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
    // 페이지네이션 관련 변수
    int pageSize = 10; // 페이지당 게시글 수
    int totalPages = (int) Math.ceil((double) postCount / pageSize); // 전체 페이지 수
    int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1; // 현재 페이지 번호
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
    <a href="?page=<%= i %>"><%= i %></a>
    <%
            }
        }
    %>
</div>
<%
    }
%>
