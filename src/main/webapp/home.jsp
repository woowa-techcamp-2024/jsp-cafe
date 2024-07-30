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
      <div>게시글 목록</div>
      <div id="info-card-explain">안녕하세요, 찬우 카페입니다!</div>
    </div>
    <div id="posts-information">
      <div>전체 글 <c:out value="${postsCount}" />개</div>
      <a id="write-button" href="${pageContext.request.contextPath}/posts/write">글쓰기</a>
    </div>
    <div id="posts">
      <div id="posts-header">
        <div id="left-header">제목</div>
        <div id="right-header">
          <div class="post-element">작성자</div>
          <div class="post-element">작성일</div>
          <div class="post-element">조회수</div>
        </div>
      </div>
      <c:forEach var="post" items="${posts}">
        <a class="post" href="/posts/<c:out value="${post.id()}" />">
          <div class="left-post">
            <c:out value="${post.title()}" />
          </div>
          <div class="right-post">
            <div class="post-element"><c:out value="${post.writer()}" /></div>
            <div class="post-element"><c:out value="${post.writtenAt()}" /></div>
            <div class="post-element"><c:out value="${post.viewCount()}" /></div>
          </div>
        </a>
      </c:forEach>
    </div>
  </div>
</div>
</body>
</html>
