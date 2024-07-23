<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.example.domain.User" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User List</title>
    <link rel="stylesheet" href="/css/common.css">
</head>
<body>
<%
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    List<User> users = (List<User>)request.getAttribute("users");
%>
<div class="container">
    <header>
        <h1>HELLO, WEB!</h1>
        <div class="buttons">
            <a href="/articles">
                <button class="btn">글 목록</button>
            </a>
            <a href="/users">
                <button class="btn">사용자 목록</button>
            </a>
            <button class="btn">로그인</button>
            <button class="btn">회원가입</button>
        </div>
    </header>
    <main>
        <div class="info-box">
            <h2>멤버리스트</h2>
            <p>참여자를 확인할 수 있습니다.</p>
        </div>
        <table>
            <thead>
            <tr>
                <th>닉네임</th>
                <th>이메일</th>
                <th>회원가입일</th>
            </tr>
            </thead>
            <tbody>
            <% for(User user : users) { %>
                <tr class="clickable-tr" onclick="goToUserDetail('<%= user.getUserId() %>')">
                    <td><%= user.getNickname() %></td>
                    <td><%= user.getEmail() %></td>
                    <td><%= user.getCreatedDt().format(formatter) %></td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </main>
</div>
<script>
    function goToUserDetail(userId) {
        window.location.href = '/users/' + userId;
    }
</script>
</body>
</html>
