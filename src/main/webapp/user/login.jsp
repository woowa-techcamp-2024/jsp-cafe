<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="/css/common.css">
</head>
<body>
<div class="container">
    <%@ include file="/common/header.jsp" %>
    <main>
        <h2>로그인</h2>
        <form method="post" action="/api/login">
            <div class="form-group">
                <label for="email">이메일</label>
                <input name="email" type="email" placeholder="이메일을 입력해주세요">
            </div>
            <div class="form-group">
                <label for="password">비밀번호</label>
                <input name="password" type="password" placeholder="비밀번호를 입력해주세요">
            </div>
            <button type="submit" class="btn-submit">로그인</button>
        </form>
        <p>아직 회원가입을 안하셨나요? <a href="/users/register">회원가입하기</a></p>
    </main>
</div>
</body>
</html>