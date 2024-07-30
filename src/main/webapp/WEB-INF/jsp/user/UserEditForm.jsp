<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="kr">
<head>
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
<%@ include file="../Header.jsp"%>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4 class="panel-title">개인정보 수정</h4>
            </div>
            <div class="panel-body">
                <form name="question" method="post" action="/users/${user.userId}">
                    <div class="form-group">
                        <label for="userId">사용자 아이디</label>
                        <input class="form-control" id="userId" name="userId" placeholder="User ID"
                               value="${user.userId}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="password">비밀번호</label>
                        <input type="password" class="form-control" id="password" name="password"
                               placeholder="Password">
                    </div>
                    <div class="form-group">
                        <label for="name">이름</label>
                        <input class="form-control" id="name" name="name" placeholder="Name" value="${user.name}">
                    </div>
                    <div class="form-group">
                        <label for="email">이메일</label>
                        <input type="email" class="form-control" id="email" name="email" placeholder="Email"
                               value="${user.email}">
                    </div>
                    <button type="submit" class="btn btn-success clearfix pull-right">수정하기</button>
                    <div class="clearfix"/>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<script src="../../../js/jquery-2.2.0.min.js"></script>
<script src="../../../js/bootstrap.min.js"></script>
<script src="../../../js/scripts.js"></script>
</body>
</html>