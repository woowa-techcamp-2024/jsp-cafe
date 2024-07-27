<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/post-details.css">
  <title>찬우 카페</title>
</head>
<body>
<div id="container">
  <div id="header">
    <a id="greeting" href="/">찬우 카페</a>
    <a id="login-button" href="/users/login">로그인/회원가입</a>
  </div>
  <div id="post-container">
    <a id="back-button" style="margin-bottom: 20px" href="/">
      <img src="${pageContext.request.contextPath}/static/assets/arrow-left.svg" alt="">
    </a>
    <div id="post-title"><c:out value="${post.title()}" /></div>
    <div id="post-header">
      <div class="header-element">작성자: <c:out value="${post.writer()}" /></div>
      <div style="display: flex; flex-direction: row; gap: 20px">
        <div class="header-element">작성일자: <c:out value="${post.writtenAt()}" /></div>
        <div class="header-element">조회수 <c:out value="${post.viewCount()}" /></div>
      </div>
    </div>
    <div id="post-content">
      <c:forTokens items="${post.content()}" delims="${separator}" var="line">
        <c:out value="${line}" /><br>
      </c:forTokens>
    </div>
    <%--<div id="comments-count">댓글 <c:out value="${commentsCount}" />개</div>
    <div id="comments-container">
      <c:forEach var="comment" items="${comments}">
        <div class="comment">
          <div class="comment-writer"><c:out value="${comment.writer()}" /></div>
          <p class="comment-content"><c:out value="${comment.content()}" /></p>
          <div class="comment-date"><c:out value="${comment.writtenAt()}" /></div>
        </div>
      </c:forEach>
    </div>
    <form style="margin-bottom: 20px">
      <div class="form-element">
        <div class="form-label"><c:out value="${user.nickname()}" /></div>
        <textarea name="content" required maxlength="200" id="content" placeholder="악플은 싫어요"></textarea>
        <div style="display: flex; justify-content: end;">
          <button id="comment-submit" type="submit" disabled>댓글 작성</button>
        </div>
      </div>
    </form>--%>
    <div id="comments-count">댓글 3개</div>
    <div id="comments-container">
      <div class="comment">
        <div class="comment-writer">사용자</div>
        <p class="comment-content">게시글에 댓글을 쓰면 이런 형식으로 보여집니다!</p>
        <div class="comment-date">2024.07.25 22:47</div>
      </div>
      <div class="comment">
        <div class="comment-writer">사용자</div>
        <p class="comment-content">게시글에 댓글을 쓰면 이런 형식으로 보여집니다!</p>
        <div class="comment-date">2024.07.25 22:47</div>
      </div>
      <div class="comment">
        <div class="comment-writer">사용자</div>
        <p class="comment-content">게시글에 댓글을 쓰면 이런 형식으로 보여집니다!</p>
        <div class="comment-date">2024.07.25 22:47</div>
      </div>
    </div>
    <form style="margin-bottom: 20px">
      <div class="form-element">
        <div class="form-label">사용자</div>
        <textarea name="content" required maxlength="200" id="content" placeholder="악플은 싫어요"></textarea>
        <div style="display: flex; justify-content: end;">
          <button id="comment-submit" type="submit" disabled>댓글 작성</button>
        </div>
      </div>
    </form>
    <div style="display: flex; flex-direction: row; justify-content: space-between; margin-bottom: 40px">
      <a class="<c:out value='${post.pageInfo().hasPrevious() ? "move-post" : "move-post-disabled"}'/>"
         href="<c:url value='/posts/${post.pageInfo().previousPostId()}'/>"
      >⬅ 이전
        글</a>
      <a class="<c:out value='${post.pageInfo().hasNext() ? "move-post" : "move-post-disabled"}'/>"
         href="<c:url value='/posts/${post.pageInfo().nextPostId()}'/>"
      >다음 글 ➡</a>
    </div>
  </div>
</div>
<script>
  document.addEventListener('DOMContentLoaded', function () {
    const textarea = document.getElementById('content');
    const submitButton = document.getElementById('comment-submit');

    textarea.addEventListener('input', function () {
      submitButton.disabled = textarea.value.trim() === '';
    });

    document.querySelectorAll('.move-post-disabled')
      .forEach(function (link) {
        link.addEventListener('click', function (event) {
          event.preventDefault();
        });
      });
  });
</script>
</body>
</html>
