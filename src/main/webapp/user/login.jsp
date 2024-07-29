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
  <jsp:include page="${pageContext.request.contextPath}/common/header.jsp" />
  <form id="user-form" method="post" action="${pageContext.request.contextPath}/auth/login">
    <div id="form-title">
      로그인
    </div>
    <c:if test="${error}">
      <div id="error-box">⚠️아이디 또는 비밀번호를 확인해주세요.</div>
    </c:if>
    <div class="form-element">
      <div class="form-label">이메일</div>
      <input autofocus required type="email" id="username" name="username" placeholder="이메일을 입력해주세요">
    </div>
    <div class="form-element">
      <div class="form-label">비밀번호</div>
      <input required type="password" id="password" name="password" placeholder="비밀번호를 입력해주세요">
    </div>
    <button id="login-submit">로그인!</button>
    <div id="register-box">
      <div id="register-message">아직도 회원가입을 안 하셨다구요?! &nbsp</div>
      <a id="register-link" href="${pageContext.request.contextPath}/users/signup">회원가입하기</a>
    </div>
  </form>
</div>
</body>
</html>