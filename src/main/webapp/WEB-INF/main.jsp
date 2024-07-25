<%@ page import="org.example.entity.Article" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="UTF-8">
    <title>Articles</title>
    <link href="<c:url value="/static/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/static/css/styles.css"/>" rel="stylesheet">
</head>
<%@ include file="navbar.jspf" %>
<body>
<div class="container" id="main">
    <h1>Articles</h1>
    <c:forEach var="article" items="${articles}">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">${article.title}</h3>
            </div>
            <div class="panel-body">
                <p><strong>Author:</strong> ${article.author}</p>
                <p>${article.content}</p>
            </div>
        </div>
    </c:forEach>
    <%
        if (request.getAttribute("articles") == null) {
            throw new RuntimeException("articles is null");
        }
    %>
</div>
<script src="../static/js/jquery-2.2.0.min.js"></script>
<script src="../static/js/bootstrap.min.js"></script>
<script src="../static/js/scripts.js"></script>
</body>
</html>