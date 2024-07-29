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
  <form id="user-form" method="post" action="${pageContext.request.contextPath}/users/signup">
    <div id="form-title">
      회원가입
    </div>
    <c:if test="${not empty  error}">
      <div id="error-box"><c:out value="${error}" /></div>
    </c:if>
    <div class="form-element">
      <div class="form-label">이메일</div>
      <input autofocus required type="email" id="username" name="username" placeholder="이메일을 입력해주세요">
    </div>
    <div class="form-element">
      <div class="form-label">닉네임</div>
      <input required type="text" id="nickname" name="nickname" placeholder="닉네임을 입력해주세요">
    </div>
    <div class="form-element">
      <div class="form-label">비밀번호</div>
      <input required type="password" id="password" name="password" placeholder="비밀번호를 입력해주세요">
    </div>
    <button id="login-submit">회원가입!</button>
  </form>
</div>
</body>
</html>
