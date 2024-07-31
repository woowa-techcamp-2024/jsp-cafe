<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HELLO, WEB!</title>
    <link rel="stylesheet" href="/css/common.css">
</head>
<body>
<div class="container">
    <%@ include file="/common/header.jsp" %>
    <main>
        <h2>회원가입</h2>
        <form method="post" action="/api/users">
            <div class="form-group">
                <label for="email">이메일</label>
                <input name="email" type="email" id="email" placeholder="이메일을 입력해주세요">
            </div>
            <div class="form-group">
                <label for="nickname">닉네임</label>
                <input name="nickname" type="text" id="nickname" placeholder="닉네임을 입력해주세요">
            </div>
            <div class="form-group">
                <label for="password">비밀번호</label>
                <input name="password" type="password" id="password" placeholder="비밀번호를 입력해주세요">
            </div>
            <button type="submit" class="btn">회원가입</button>
        </form>
    </main>
</div>
</body>
</html>
