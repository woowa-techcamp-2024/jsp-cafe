<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="../static/css/style.css">
  <link rel="stylesheet" href="../static/css/main.css">
  <link rel="stylesheet" href="../static/css/profile.css">
  <title>찬우 카페</title>
</head>
<body>
<div id="container">
  <div id="header">
    <a id="greeting" href="/">찬우 카페</a>
    <a id="login-button" href="/users/login">로그인/회원가입</a>
  </div>
  <div id="back-header">
    <a id="back-button" href="/users">
      <img src="../static/assets/arrow-left.svg" alt="">
    </a>
  </div>
  <div
      style="background-image: url('https://avatars.githubusercontent.com/u/69714701');"
      id="profile-image">
  </div>
  <div id="nickname"><c:out value="${profile.nickname()}" /></div>
  <div id="email"><c:out value="${profile.email()}" /></div>
  <div id="sign-up-date"><c:out value="${profile.signUpAt()}" /></div>
</div>
</body>
</html>
