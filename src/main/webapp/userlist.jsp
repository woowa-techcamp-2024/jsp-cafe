<%@ page import="java.util.List" %>
<%@ page import="org.example.jspcafe.user.response.UserListResponse" %>
<%@ page import="org.example.jspcafe.user.response.UserResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>사용자 리스트</title>
    <link rel="stylesheet" href="./css/header.css">
    <link rel="stylesheet" href="./css/common.css">
    <link rel="stylesheet" href="./css/user-list.css">
</head>
<body>
<div class="container">
    <header class="header">
        <h1 class="header-title"><a href="/">HELLO. WEB!</a></h1>
        <nav>
            <%
                Boolean isLogined = (Boolean) session.getAttribute("isLogined");
                String nickname = (String) session.getAttribute("nickname");
                if (isLogined != null && isLogined) {
            %>
            <span class="user-name">환영합니다, <%= nickname %>!</span>
            <form action="api/logout" method="post" style="display: inline;">
                <button type="submit" class="logout-button">로그아웃</button>
            </form>
            <form action="edit-profile" method="get" style="display: inline;">
                <button type="submit" class="edit-profile-button">정보수정</button>
            </form>
            <% } else { %>
            <form action="login" method="get" style="display: inline;">
                <button type="submit" class="login-button">로그인</button>
            </form>
            <form action="signup" method="get" style="display: inline;">
                <button type="submit" class="signup-button">회원가입</button>
            </form>
            <% } %>
            <form action="users" method="get" style="display: inline;">
                <button type="submit" class="user-list-button">사용자 목록</button>
            </form>
        </nav>
    </header>
    <div class="wrapper">
        <h1>사용자 목록</h1>
        <%
            UserListResponse userList = (UserListResponse) request.getAttribute("userList");
            if (userList != null) {
        %>
        <p class="user-count">총 사용자 수: <%= userList.count() %>명</p>
        <div class="user-list-container">
            <div class="user-list">
                <table class="user-list">
                    <thead>
                    <tr>
                        <th>닉네임</th>
                        <th>이메일</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        for (UserResponse user : userList.userList()) {
                    %>
                    <tr class="user-list__item">
                        <td class="user-list__item__name">
                            <a href="users/<%= user.nickname() %>" class="profile-link"><%= user.nickname() %></a>
                        </td>
                        <td class="user-list__item__email"><%= user.email() %></td>
                    </tr>
                    <%
                        }
                    %>
                    </tbody>
                </table>
            </div>
        </div>
        <%
        } else {
        %>
        <div class="user-list__error-container">
            <p class="user-list__error">사용자 목록을 불러올 수 없습니다.</p>
        </div>
        <%
            }
        %>
    </div>
</div>
</body>
</html>
