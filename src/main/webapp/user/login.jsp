<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/login.css">
  <title>찬우 카페</title>
</head>
<body>
<div id="container">
  <div id="header">
    <a id="greeting" href="/">찬우 카페</a>
    <a id="login-button" href="/users/login">로그인/회원가입</a>
  </div>
  <form id="user-form">
    <div id="form-title">
      로그인
    </div>
    <div class="form-element">
      <div class="form-label">이메일</div>
      <input autofocus required type="email" id="username" placeholder="이메일을 입력해주세요">
    </div>
    <div class="form-element">
      <div class="form-label">비밀번호</div>
      <input required type="password" id="password" placeholder="비밀번호를 입력해주세요">
    </div>
    <button id="login-submit">로그인!</button>
    <div id="register-box">
      <div id="register-message">아직도 회원가입을 안 하셨다구요?! &nbsp</div>
      <a id="register-link" href="/users/signup">회원가입하기</a>
    </div>
  </form>
</div>
</body>
</html>