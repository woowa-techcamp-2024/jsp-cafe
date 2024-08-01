<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/error.css">
  <title>찬우 카페</title>
</head>
<body>
<div id="container">
  <div class="error-message">인증에 실패했어요!</div>
  <div class="error-message">401 Unauthorized<br>(까먹으신 건 아니겠죠??)</div>
  <div style="margin-top: 20px"><c:out value="${requestScope.errorMessage}" /></div>
  <a id="home-button" href="${pageContext.request.contextPath}/">홈으로 가기</a>
</div>
</body>
</html>
