<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/profile.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/login.css">
  <title>찬우 카페</title>
</head>
<body>
<div id="container">
  <jsp:include page="${pageContext.request.contextPath}/common/header.jsp" />
  <div id="back-header">
    <a id="back-button" href="${pageContext.request.contextPath}/users">
      <img src="${pageContext.request.contextPath}/static/assets/arrow-left.svg" alt="">
    </a>
  </div>
  <div
      style="background-image: url('https://avatars.githubusercontent.com/u/69714701');"
      id="profile-image">
  </div>
  <form method="post" action="/users/profile/${profile.id()}">
    <div class="form-element">
      <div class="form-label">닉네임</div>
      <input autofocus required value="${profile.nickname()}" type="text" id="nickname" name="nickname"
             placeholder="멋진 닉네임">
    </div>
    <div class="form-element">
      <div class="form-label">기존 비밀번호</div>
      <input required type="password" id="password" name="password" placeholder="비밀번호가 뭐였지?">
    </div>
    <button id="login-submit">회원 정보 변경!</button>
  </form>
</div>
</body>
</html>
