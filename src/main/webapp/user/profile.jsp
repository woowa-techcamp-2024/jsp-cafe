<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.example.domain.User" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
    <link rel="stylesheet" href="/css/common.css">
</head>
<body>
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

        <div class="info-box">
            <h2>사용자 상세 정보</h2>
            <p>사용자의 상세 정보를 확인할 수 있습니다.</p>
        </div>

        <%
            User user = (User) request.getAttribute("user");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        %>

        <table>
            <tr>
                <th>닉네임</th>
                <td><%= user.getNickname() %></td>
            </tr>
            <tr>
                <th>이메일</th>
                <td><%= user.getEmail() %></td>
            </tr>
            <tr>
                <th>가입일</th>
                <td><%= user.getCreatedDt().format(formatter) %></td>
            </tr>
        </table>
        <div class="action-buttons">
            <a href="/users/update-form/${user.userId}">
                <button class="btn">수정 하기</button>
            </a>
        </div>
    </div>
</body>
</html>
