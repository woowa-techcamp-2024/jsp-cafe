<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.example.domain.Article" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 상세보기</title>
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/article/detail.css">
</head>
<body>
<%
    Article article = (Article) request.getAttribute("article");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
%>
    <div class="container">
        <header>
            <h1>HELLO, WEB!</h1>
            <div class="buttons">
                <a href="/users">
                    <button class="btn">사용자 목록</button>
                </a>
                <button class="btn">로그인</button>
                <button class="btn">회원가입</button>
            </div>
        </header>
        <main>
            <h2 class="post-title"><%= article.getTitle() %></h2>
            <div class="post-info">
                <span>작성자: <%= article.getAuthor() %></span>
                <span>작성일: <%= article.getCreatedDt().format(formatter) %></span>
            </div>
            <div class="post-content">
                <p>
                    <%= article.getContent() %>
                </p>
            </div>
            <div class="action-buttons">
                <a href="/articles">
                    <button class="btn">글 목록</button>
                </a>
            </div>
        </main>
    </div>
</body>
</html>