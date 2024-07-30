<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="common.jspf" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${article.title}</title>
    <link href="<c:url value='/static/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/static/css/styles.css'/>" rel="stylesheet">
</head>
<%@ include file="navbar.jspf" %>
<body>
<div class="container" id="main">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h1 class="panel-title">제목: ${article.title}</h1>
        </div>
        <div class="panel-body">
            <p><strong>Author:</strong> ${article.author}</p>
            <p>글내용: ${article.content}</p>
            <a href="<c:url value='/question/${article.articleId}/updateForm'/>" class="btn btn-warning">Edit</a>
            <button id="deleteButton" class="btn btn-danger">Delete</button>
        </div>
    </div>

    <!-- 댓글 목록 -->
    <div class="panel panel-default">
        <div class="panel-heading">
            <h2 class="panel-title">댓글</h2>
        </div>
        <div class="panel-body replies">
            <c:forEach var="reply" items="${replies}">
                <div class="reply">
                    <p><strong>${reply.authorId}:</strong> ${reply.content}
                        <button class="btn btn-danger btn-sm delete-reply-button" data-reply-id="${reply.replyId}" style="margin-left: 10px;">Delete</button>
                    </p>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- 댓글 작성 폼 -->
    <div class="panel panel-default">
        <div class="panel-heading">
            <h2 class="panel-title">댓글 작성</h2>
        </div>
        <div class="panel-body">
            <form id="replyForm">
                <div class="form-group">
                    <label for="replyContent">내용</label>
                    <textarea class="form-control" id="replyContent" rows="3" required></textarea>
                </div>
                <button type="submit" class="btn btn-primary">댓글 작성</button>
            </form>
        </div>
    </div>
</div>
<script>
  document.getElementById('deleteButton').addEventListener('click', function() {
    const xhr = new XMLHttpRequest();
    xhr.open('DELETE', '<c:url value="/question/${article.articleId}"/>', true);
    xhr.onload = function() {
      if (xhr.status === 200) {
        window.location.href = '/';
      } else {
        window.location.href = '/error/not-same-author.html';
      }
    };
    xhr.send();
  });

  document.getElementById('replyForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const content = document.getElementById('replyContent').value;
    const articleId = ${article.articleId};
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '<c:url value="/reply"/>', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function() {
      if (xhr.status === 201 || xhr.status === 200) {
        const articleId = ${article.articleId};
        const xhrReplies = new XMLHttpRequest();
        xhrReplies.open('GET', '/question/' + articleId + '/replies', true);
        xhrReplies.onload = function() {
          if (xhrReplies.status === 200) {
            const responseJson = JSON.parse(xhrReplies.responseText);
            const repliesHtml = responseJson.replies.map(reply => `
              <div class="reply">
                <p><strong><c:out value="\${reply.authorId}"/></strong>: <c:out value="\${reply.content}"/>
                <button class="btn btn-danger btn-sm delete-reply-button" data-reply-id="<c:out value="\${reply.replyId}"/>" style="margin-left: 10px;">Delete</button>
                </p>
              </div>
            `).join('');
            document.querySelector('.replies').innerHTML = repliesHtml;
          } else {
            alert('댓글 목록 갱신에 실패했습니다.');
          }
        };
        xhrReplies.send();
      } else {
        alert('댓글 작성에 실패했습니다.');
      }
    };
    xhr.send(JSON.stringify({ content: content, articleId: articleId }));
  });

  document.querySelector('.replies').addEventListener('click', function(event) {
    if (event.target.classList.contains('delete-reply-button')) {
      const replyId = event.target.getAttribute('data-reply-id');
      const xhr = new XMLHttpRequest();
      xhr.open('DELETE', '/reply/' + replyId, true);
      xhr.onload = function() {
        if (xhr.status === 200) {
          const articleId = ${article.articleId};
          const xhrReplies = new XMLHttpRequest();
          xhrReplies.open('GET', '/question/' + articleId + '/replies', true);
          xhrReplies.onload = function() {
            if (xhrReplies.status === 200) {
              const responseJson = JSON.parse(xhrReplies.responseText);
              const repliesHtml = responseJson.replies.map(reply => `
                <div class="reply">
                  <p><strong><c:out value="\${reply.authorId}"/></strong>: <c:out value="\${reply.content}"/>
                  <button class="btn btn-danger btn-sm delete-reply-button" data-reply-id="<c:out value="\${reply.replyId}"/>" style="margin-left: 10px;">Delete</button>
                  </p>
                </div>
              `).join('');
              document.querySelector('.replies').innerHTML = repliesHtml;
            } else {
              alert('댓글 목록 갱신에 실패했습니다.');
            }
          };
          xhrReplies.send();
        } else {
          alert('댓글 삭제에 실패했습니다.');
        }
      };
      xhr.send();
    }
  });
</script>
</body>
</html>