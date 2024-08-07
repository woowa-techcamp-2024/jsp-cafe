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
  <jsp:include page="${pageContext.request.contextPath}/common/header.jsp" />
  <div id="post-container">
    <a id="back-button" style="margin-bottom: 20px" href="${pageContext.request.contextPath}/">
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
    <c:choose>
      <c:when test="${sessionScope.authentication.principal().getNickname() == post.writer()}">
        <div id="modify-buttons">
          <a id="edit-button" class="button" href="${pageContext.request.contextPath}/posts/edit/${post.id()}">수정</a>
          <div id="delete-button" class="button" href="${pageContext.request.contextPath}/posts"
               onclick="handleDeleteSubmit()"
          >삭제
          </div>
        </div>
      </c:when>
    </c:choose>
    <div id="comments-count">댓글 <c:out value="${replies.size()}" />개</div>
    <div id="comments-container">
      <c:forEach var="reply" items="${replies}">
        <div class="comment" id="<c:out value="comment-${reply.id()}"/>">
          <div class="comment-writer"><c:out value="${reply.writer()}" /></div>
          <p class="comment-content"><c:out value="${reply.content()}" /></p>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <div class="comment-date"><c:out value="${reply.writtenAt()}" /></div>
            <c:choose>
              <c:when test="${reply.writer() == sessionScope.authentication.principal().getNickname()}">
                <div class="comment-delete-button" onclick="handleReplyDelete(${reply.id()})">삭제</div>
              </c:when>
            </c:choose>
          </div>
        </div>
      </c:forEach>
    </div>
    <div style="margin-bottom: 20px">
      <div class="form-element">
        <div class="form-label"><c:out value="${user.nickname()}" /></div>
        <textarea name="content" required maxlength="200" id="content" placeholder="악플은 싫어요"></textarea>
        <input type="hidden" name="postId" value="<c:out value="${post.id()}" />">
        <div style="display: flex; justify-content: end;">
          <button id="comment-submit" type="submit" disabled>댓글 작성</button>
        </div>
      </div>
    </div>
  </div>
</div>
<script>
  document.addEventListener('DOMContentLoaded', () => {
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

    submitButton.addEventListener('click', function () {
      const content = textarea.value.trim();
      const postId = parseInt(document.querySelector('input[name="postId"]').value, 10);

      fetch('${pageContext.request.contextPath}/replies', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          content: content,
          postId: postId
        })
      }).then(response => {
        if (response.ok) {
          response.json()
            .then(reply => {
              const commentsContainer = document.getElementById('comments-container');
              const comment = document.createElement('div');
              comment.className = 'comment';
              comment.id = 'comment-' + reply.id;
              comment.innerHTML = '<div class="comment-writer">' + reply.writer + '</div>' +
                '<p class="comment-content">' + reply.content + '</p>' +
                '<div style="display: flex; justify-content: space-between; align-items: center;">' +
                '<div class="comment-date">' + reply.writtenAt + '</div>' +
                '<div class="comment-delete-button" onclick="handleReplyDelete(' + reply.id + ')">삭제</div>' +
                '</div>';
              commentsContainer.appendChild(comment);
              textarea.value = '';
              submitButton.disabled = true;
            });
        }
      });
    });
  });

  const handleDeleteSubmit = () => {
    if (confirm('게시글을 삭제하시겠습니까?')) {
      fetch('${pageContext.request.contextPath}/posts/${post.id()}', {
        method: 'DELETE',
      }).then(response => {
        if (response.ok) {
          window.location.href = '${pageContext.request.contextPath}/';
        } else {
          response.text().then(body => {
            document.open();
            document.write(body);
            document.close();
          });
        }
      });
    }
  };

  const handleReplyDelete = (replyId) => {
    if (confirm('댓글을 삭제하시겠습니까?')) {
      fetch('${pageContext.request.contextPath}/replies/' + replyId, {
        method: 'DELETE',
      }).then(response => {
        if (response.ok) {
          document.getElementById('comment-' + replyId).remove();
        } else {
          response.text()
            .then(body => {
              document.open();
              document.write(body);
              document.close();
            });
        }
      });
    }
  };
</script>
</body>
</html>
