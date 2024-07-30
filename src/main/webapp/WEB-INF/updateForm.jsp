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
            <form id="updateForm" action="<c:url value='/question/${article.id}'/>" method="POST">
                <input type="hidden" name="_method" value="PUT">
                <div class="form-group">
                    <label for="title">Title</label>
                    <input type="text" class="form-control" id="title" name="title" value="${article.title}" required>
                </div>
                <div class="form-group">
                    <label for="content">Content</label>
                    <textarea class="form-control" id="content" name="content" rows="10" required>${article.content}</textarea>
                </div>
                <button type="submit" class="btn btn-primary">Update</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>