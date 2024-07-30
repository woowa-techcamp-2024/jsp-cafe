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
</div>
<script>


  document.getElementById('deleteButton').addEventListener('click', function() {
    fetch('<c:url value="/question/${article.articleId}"/>', {
      method: 'DELETE'
    }).then(response => {
      if (response.ok) {
        window.location.href = '/';
      }else {
        window.location.href ='/error/not-same-author.html';
      }
    });
  });
</script>
</body>
</html>