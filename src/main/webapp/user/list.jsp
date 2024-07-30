<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css">
  <title>찬우 카페</title>
</head>
<body>
<div id="container">
  <jsp:include page="${pageContext.request.contextPath}/common/header.jsp" />
  <div id="posts-container">
    <div id="info-card">
      <div>멤버 리스트</div>
      <div id="info-card-explain">찬우 카페 멤버들이에요!</div>
    </div>
    <div id="posts-information">전체 멤버 <c:out value="${membersCount}" />명</div>
    <div id="posts">
      <div id="posts-header">
        <div class="post-element">닉네임</div>
        <div class="post-element">이메일</div>
        <div class="post-element">회원가입일</div>
      </div>
      <c:forEach var="member" items="${members}">
        <a class="post" href="/users/<c:out value="${member.id()}" />">
          <div class="post-element" style="font-size: 1.1em; font-weight: 600;">
            <c:out value="${member.nickname()}" />
          </div>
          <div class="post-element">
            <c:out value="${member.email()}" />
          </div>
          <div class="post-element">
            <c:out value="${member.createdAt()}" />
          </div>
        </a>
      </c:forEach>
    </div>
  </div>
</div>
</body>
</html>
