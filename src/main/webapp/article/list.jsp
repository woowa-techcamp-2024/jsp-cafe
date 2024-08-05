<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.example.domain.Article" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Article List</title>
    <link rel="stylesheet" href="/css/common.css">
</head>
<body>
<%
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    List<Article> articles = (List<Article>)request.getAttribute("articles");
    int currentPage = (Integer) request.getAttribute("currentPage");
    int totalPageNumber = (Integer) request.getAttribute("totalPageNumber");

    int pageGroup = 5; // 페이지 그룹 크기
    int currentGroup = (int)Math.ceil((double)currentPage / pageGroup);
    int startPage = (currentGroup - 1) * pageGroup + 1;
    int endPage = Math.min(currentGroup * pageGroup, totalPageNumber);
%>
    <div class="container">
        <%@ include file="/common/header.jsp" %>
        <div class="info-box">
            <h2>게시글 목록</h2>
            <p>게시글을 확인할 수 있습니다.</p>
        </div>

        <main>
            <table>
                <thead>
                    <tr>
                        <th>제목</th>
                        <th>작성자</th>
                        <th>작성일자</th>
                    </tr>
                </thead>
                <tbody>
                <% for(Article article : articles) { %>
                        <tr class="clickable-tr" onclick="goToArticleDetail('<%= article.getArticleId() %>')">
                            <td><%= article.getTitle() %></td>
                            <td><%= article.getAuthor() %></td>
                            <td><%= article.getCreatedDt().format(formatter) %></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
            <div class="pagination">
                <% if (startPage > 1) { %>
                    <a href="?page=<%= startPage - pageGroup %>" class="page-link prev">&laquo;</a>
                <% } %>

                <% for (int i = startPage; i <= endPage; i++) { %>
                    <% if (i == currentPage) { %>
                        <a href="?page=<%= i %>" class="page-link active"><%= i %></a>
                    <% } else { %>
                        <a href="?page=<%= i %>" class="page-link"><%= i %></a>
                    <% } %>
                <% } %>

                <% if (endPage < totalPageNumber) { %>
                    <a href="?page=<%= endPage + 1 %>" class="page-link next">&raquo;</a>
                <% } %>
            </div>
            <div class="action-buttons">
                <a href="/articles/register">
                    <button class="btn">글쓰기</button>
                </a>
            </div>
        </main>
    </div>
<script>
    function goToArticleDetail(articleId) {
        window.location.href = '/articles/' + articleId;
    }
</script>
</body>
</html>