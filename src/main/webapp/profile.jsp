<%@ page import="org.example.jspcafe.user.response.UserResponse" %>
<%@ page import="org.example.jspcafe.user.response.UserProfileResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>프로필</title>
    <link rel="stylesheet" href="../css/header.css">
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/profile.css">
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
            <form action="/api/logout" method="post" style="display: inline;">
                <button type="submit" class="logout-button">로그아웃</button>
            </form>
            <form action="editProfile" method="get" style="display: inline;">
                <button type="submit" class="edit-profile-button">정보수정</button>
            </form>
            <% } else { %>
            <form action="/login" method="get" style="display: inline;">
                <button type="submit" class="login-button">로그인</button>
            </form>
            <form action="/signup" method="get" style="display: inline;">
                <button type="submit" class="signup-button">회원가입</button>
            </form>
            <% } %>
            <form action="/users" method="get" style="display: inline;">
                <button type="submit" class="user-list-button">사용자 목록</button>
            </form>
        </nav>
    </header>
    <div class="wrapper">
        <h1>사용자 프로필</h1>
        <%
            UserProfileResponse userProfile = (UserProfileResponse) request.getAttribute("userProfile");
        %>
        <div class="profile-container">
            <div class="profile-box">
                <h2 class="profile-nickname">닉네임: <%= userProfile.nickname() %></h2>
                <p class="profile-email">이메일: <%=  userProfile.email() %></p>
                <p class="profile-created-at">가입일: <%= userProfile.createdAt() %></p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
