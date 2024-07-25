<%@ page import="org.example.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="common.jspf" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>User Profile</title>
    <link rel="stylesheet" href="../../static/css/bootstrap.min.css">
</head>
<%@ include file="navbar.jspf" %>
<body>
<%
    User user = (User) request.getAttribute("user");
    if (user != null) {
        String userId = user.getUserId();
        if (userId != null) {
            String nickname = user.getNickname();
            String email = user.getEmail();
%>
<div class="container">
    <h1>개인정보 수정</h1>
    <form id="profileForm">
        <div class="form-group">
            <label for="userId">사용자 아이디</label>
            <input type="text" class="form-control" id="userId" name="userId" value="<%= userId %>" readonly>
        </div>
        <div class="form-group">
            <label for="name">이름</label>
            <input type="text" class="form-control" id="name" name="name" value="<%= nickname %>" required>
        </div>
        <div class="form-group">
            <label for="email">이메일</label>
            <input type="email" class="form-control" id="email" name="email" value="<%= email %>" required>
        </div>
        <div class="form-group">
            <label for="password">비밀번호</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <button type="button" class="btn btn-primary" onclick="submitForm()">수정</button>
    </form>
</div>
<script>
  function getUserIdFromUrl() {
    const pathArray = window.location.pathname.split('/');
    return pathArray[pathArray.length - 2]; // Assuming userId is second last in the path
  }
  function submitForm() {
    const form = document.getElementById('profileForm');
    const formData = new FormData(form);
    const object = Object.fromEntries(formData.entries());
    const json = JSON.stringify(object);
    const link = "/users/" + getUserIdFromUrl();
    console.log(link)

    fetch(link, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      body: json
    })
    .then(response => {
      if (response.ok) {
        alert('개인정보가 성공적으로 수정되었습니다.');
        window.location.reload();
      } else {
        alert('개인정보 수정에 실패했습니다. 비밀번호를 확인해주세요.');
      }
    });
  }
</script>
<%
} else {
%>
<div class="container" id="main">
    <div class="col-md-10 col-md-offset-1">
        <h1>유효하지 않은 사용자입니다. <a href="<c:url value='/login' />">로그인</a>하십시오.</h1>
    </div>
</div>
<%
    }
} else {
%>
<div class="container" id="main">
    <div class="col-md-10 col-md-offset-1">
        <h1>로그인이 필요합니다. <a href="<c:url value='/login' />">로그인</a></h1>
    </div>
</div>
<%
    }
%>
</body>
</html>
