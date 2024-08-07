<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="utf-8">
    <title>500 - Internal Server Error</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="<c:url value='/static/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/static/css/styles.css'/>" rel="stylesheet">
</head>
<body>
<%@ include file="../Header.jsp"%>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <div class="panel-body">
                <h1 class="text-center">500 - Internal Server Error</h1>
                <p class="text-center">죄송합니다. 서버에 문제가 발생했습니다.</p>
                <p class="text-center">잠시 후 다시 시도해 주세요.</p>
                <div class="text-center">
                    <a href="<c:url value='/'/>" class="btn btn-primary">홈으로 돌아가기</a>
                </div>
                <p class="text-center mt-3">문제가 지속되면 관리자에게 연락해 주세요: admin@example.com</p>
            </div>
        </div>
    </div>
</div>

<script src="<c:url value='/js/jquery-2.2.0.min.js'/>"></script>
<script src="<c:url value='/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/js/scripts.js'/>"></script>
</body>
</html>