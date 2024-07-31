<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.example.domain.User" %>
<%@ page import="org.example.constance.SessionName" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
    <link rel="stylesheet" href="/css/common.css">
</head>
<body>
    <div class="container">
        <%@ include file="/common/header.jsp" %>
        <div class="info-box">
            <h2>사용자 상세 정보 수정</h2>
            <p>사용자의 상세 정보를 수정 할 수 있습니다.</p>
        </div>
        <%
            User user = (User) request.getAttribute(SessionName.USER.getName());
        %>
        <form method="post" action="/api/users/${user.userId}">
            <input type="hidden" name="userId" value="${user.userId}">
            <div class="form-group">
                <label for="email">이메일</label>
                <input name="email" type="email" id="email" value="${user.email}" readonly>
            </div>
            <div class="form-group">
                <label for="nickname">닉네임</label>
                <input name="nickname" type="text" id="nickname" value="${user.nickname}" placeholder="새로운 닉네임을 입력해주세요">
            </div>
            <div class="form-group">
                <label for="password">비밀번호 확인</label>
                <input name="password" type="password" id="password" placeholder="기존 비밀번호를 입력해주세요">
            </div>
            <button type="submit" class="btn">수정 완료</button>
        </form>
    </div>
</body>
</html>
