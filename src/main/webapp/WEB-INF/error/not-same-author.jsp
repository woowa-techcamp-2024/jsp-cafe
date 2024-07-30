<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../common.jspf" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>NOT SAME AUTHOR</title>
    <link href="<c:url value="/static/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/static/css/styles.css"/>" rel="stylesheet">
</head>
<%@ include file="../navbar.jspf" %>
<body>
<div class="container" id="main">
    <div class="panel panel-danger">
        <div class="panel-heading">
            <h1 class="panel-title">NOT SAME AUTHOR</h1>
        </div>
        <div class="panel-body">
            <p>You are not authorized edit other's posts.</p>
            <a href="<c:url value="/"/>" class="btn btn-primary">Go to Home</a>
        </div>
    </div>
</div>
</body>
</html>