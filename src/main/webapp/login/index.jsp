<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/header.css">
    <link rel="stylesheet" href="../css/login.css">
</head>
<body>
<%
    Boolean isLogined = (Boolean) session.getAttribute("isLogined");
    if (isLogined != null && isLogined) {
        response.sendRedirect("/"); // 메인 페이지로 리디렉션
        return;
    }
    String errorMessage = (String) request.getAttribute("errorMessage");
%>
<div class="container">
    <header class="header">
        <h2 class="header-title"><a href="/">HELLO. WEB!</a></h2>
        <nav>
            <form action="../login" method="get" style="display: inline;">
                <button type="submit" class="login-button">로그인</button>
            </form>
            <form action="../signup" method="get" style="display: inline;">
                <button type="submit" class="signup-button">회원가입</button>
            </form>
        </nav>
    </header>
    <main>
        <h1 class="login-title">로그인</h1>
        <form class="login-form" action="/api/login" method="post">
            <div class="input-field">
                <label for="email">이메일</label>
                <input
                        type="email"
                        id="email"
                        name="email"
                        placeholder="이메일을 입력해주세요"
                        required
                />
            </div>
            <div class="input-field">
                <label for="password">비밀번호</label>
                <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="비밀번호를 입력해주세요"
                        required
                />
            </div>
            <button type="submit" class="login-button">로그인</button>
        </form>
        <p class="signup-link">
            아직 회원가입을 안하셨나요? <a href="../signup">회원가입하기</a>
        </p>

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