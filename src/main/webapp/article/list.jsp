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