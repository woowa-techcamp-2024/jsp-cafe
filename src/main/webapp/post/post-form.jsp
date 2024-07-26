<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/post-form.css">
  <title>찬우 카페</title>
</head>
<body>
<div id="container">
  <div id="header">
    <a id="greeting" href="/">찬우 카페</a>
    <a id="login-button" href="/users/login">로그인/회원가입</a>
  </div>
  <form id="post-form" method="post" action="/posts/write">
    <div id="form-title">
      글쓰기
    </div>
    <div class="form-element">
      <div class="form-label">제목</div>
      <input type="text" autofocus required minlength="2" maxlength="30" id="title" name="title"
             placeholder="글의 제목을 입력해주세요">
    </div>
    <div class="form-element">
      <div class="form-label">내용</div>
      <textarea name="content" required minlength="2" maxlength="1000" id="content"
                placeholder="글의 내용을 입력해주세요"></textarea>
    </div>
    <button id="post-submit">작성 완료!</button>
  </form>
</div>
</body>
</html>
