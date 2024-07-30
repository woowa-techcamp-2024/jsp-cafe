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
  <div id="nickname"><c:out value="${profile.nickname()}" /></div>
  <div id="email"><c:out value="${profile.email()}" /></div>
  <div id="sign-up-date"><c:out value="${profile.signUpAt()}" /></div>
  <c:choose>
    <c:when test="${sessionScope.authentication.isPrincipal(profile.id())}">
      <a id="profile-button" href="/users/profile/${profile.id()}">프로필 수정</a>
    </c:when>
  </c:choose>
</div>
</body>
</html>
