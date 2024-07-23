<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>개인정보 수정</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/header.css">
    <link rel="stylesheet" href="/css/edit-profile.css">
</head>
<body>
<%
    Boolean isLogined = (Boolean) session.getAttribute("isLogined");
    if (isLogined == null || !isLogined) {
        response.sendRedirect("/login"); // 로그인 페이지로 리디렉션
        return;
    }
    String nickname = (String) session.getAttribute("nickname");
    String errorMessage = (String) request.getAttribute("errorMessage");
%>
<div class="container">
    <header class="header">
        <h2 class="header-title"><a href="/">HELLO. WEB!</a></h2>
        <nav>
            <%
                if (isLogined != null && isLogined) {
            %>
            <span class="user-name">환영합니다, <%= nickname %>!</span>
            <form action="api/logout" method="post" style="display: inline;">
                <button type="submit" class="logout-button">로그아웃</button>
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
    <main>
        <h1 class="edit-title">개인정보 수정</h1>
        <form class="edit-form" action="/api/edit-profile" method="post">
            <input type="hidden" name="_method" value="PATCH">
            <div class="input-field">
                <label for="nickname">닉네임</label>
                <input
                        type="text"
                        id="nickname"
                        name="nickname"
                        placeholder="닉네임을 입력해주세요"
                        value="<%= nickname %>"
                />
            </div>
            <div class="input-field">
                <label for="password">비밀번호</label>
                <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="비밀번호를 입력해주세요"
                />
            </div>
            <button type="submit" class="edit-button">수정하기</button>
        </form>

        <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
        <div id="error-message" class="error-message">
            <%= errorMessage %>
        </div>
        <script>
            document.addEventListener("DOMContentLoaded", function() {
                const errorMessage = document.getElementById('error-message');
                if (errorMessage) {
                    errorMessage.style.display = 'block';
                    setTimeout(() => {
                        errorMessage.style.display = 'none';
                    }, 3000); // 3초 후에 사라짐
                }
            });
        </script>
        <% } %>
    </main>
</div>
</body>
</html>
