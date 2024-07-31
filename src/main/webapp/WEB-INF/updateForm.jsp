<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="common.jspf" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Article</title>
    <link href="<c:url value="/static/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/static/css/styles.css"/>" rel="stylesheet">
</head>
<%@ include file="navbar.jspf" %>
<body>
<div class="container" id="main">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h1 class="panel-title">Update Article</h1>
        </div>
        <div class="panel-body">
            <div class="form-group">
                <label for="title">Title</label>
                <input type="text" class="form-control" id="title" name="title" value="${article.title}" required>
            </div>
            <div class="form-group">
                <label for="content">Content</label>
                <textarea class="form-control" id="content" name="content" rows="10" required>${article.content}</textarea>
            </div>
            <button id="updateButton" class="btn btn-primary">Update</button>
        </div>
    </div>
</div>
<script>
  document.getElementById('updateButton').addEventListener('click', function() {
    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;

    fetch('<c:url value="/question/${article.articleId}"/>', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        title: title,
        content: content
      })
    }).then(response => {
      console.log(response);
      if (response.ok) { // 2xx 범위의 응답 확인
        // 서버가 리다이렉트 URL을 응답의 Location 헤더로 보내는 경우 처리
        const redirectUrl = response.headers.get('Location');
        if (redirectUrl) {
          window.location.href = redirectUrl;
        } else {
          alert('Article updated successfully.');
        }
      }  else {
        alert('Failed to update the article.');
      }
    });
  });
</script>
</body>
</html>