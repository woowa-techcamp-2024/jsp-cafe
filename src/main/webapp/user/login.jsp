<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/login.css">
</head>
<body>
<div class="container">
    <header>
        <h1>HELLO, WEB!</h1>
        <button class="btn">로그인/회원가입</button>
    </header>
    <main>
        <h2>로그인</h2>
        <form>
            <div class="form-group">
                <input type="email" placeholder="이메일을 입력해주세요">
            </div>
            <div class="form-group">
                <input type="password" placeholder="비밀번호를 입력해주세요">
            </div>
            <button type="submit" class="btn-login">로그인</button>
        </form>
        <p class="register-link">아직 회원가입을 안하셨나요? <a href="#">회원가입하기</a></p>
    </main>
</div>
</body>
</html>