<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="<c:url value="/css/styles.css"/>" rel="stylesheet">
</head>
<body>
<%@ include file="../header.jsp" %>
<%@ include file="../nav.jsp" %>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <form name="question" method="post" action="/users/${user.userId}/edit">
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email"
                           class="form-control"
                           id="email"
                           name="email"
                           placeholder="Email"
                           value="${user.email}"
                           disabled>
                </div>
                <div class="form-group">
                    <label for="name">닉네임</label>
                    <input class="form-control"
                           id="name"
                           name="nickname"
                           placeholder="Nickname"
                           value="${user.nickname}">
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">수정</button>
                <div class="clearfix"/>
            </form>
        </div>
    </div>
</div>

<!-- script references -->
<script src="<c:url value="/js/jquery-2.2.0.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/js/scripts.js"/>"></script>
</body>
</html>
