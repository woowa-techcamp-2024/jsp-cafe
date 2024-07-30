<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/header.css">
<div id="header">
  <a id="greeting" href="${pageContext.request.contextPath}/">찬우 카페</a>
  <c:choose>
    <c:when test="${empty sessionScope.authentication}">
      <a class="login-button" href="${pageContext.request.contextPath}/login">로그인/회원가입</a>
    </c:when>
    <c:otherwise>
      <div id="buttons-container">
        <a class="login-button" href="<c:url value='/users/${sessionScope.authentication.principal().getId()}'/>">
          내 프로필
        </a>
        <a class="login-button" href="${pageContext.request.contextPath}/users">멤버들</a>
        <form id="logout-form" method="post" action="${pageContext.request.contextPath}/auth/logout">
          <button class="login-button">로그아웃</button>
        </form>
      </div>
    </c:otherwise>
  </c:choose>
</div>
