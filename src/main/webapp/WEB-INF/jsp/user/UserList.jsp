<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="<c:url value='/static/css/bootstrap.min.css'/>" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="<c:url value='/static/css/styles.css'/>" rel="stylesheet">
</head>
<body>
<
<%@ include file="../Header.jsp" %>

<div class="container" id="main">
    <div class="col-md-10 col-md-offset-1">
        <div class="panel panel-default">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>사용자 아이디</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${users}" varStatus="status">
                    <tr>
                        <th scope="row">${status.count}</th>
                        <td>${user.userId}</td>
                        <td><a href="<c:url value='/users/${user.userId}'/>">${user.name}</a></td>
                        <td>${user.email}</td>
                        <td><a href="/users/${user.userId}/form" class="btn btn-success" role="button">수정</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- script references -->
<script src="<c:url value='/js/jquery-2.2.0.min.js'/>"></script>
<script src="<c:url value='/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/js/scripts.js'/>"></script>
</body>
</html>