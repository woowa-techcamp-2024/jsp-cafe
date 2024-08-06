<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="utf-8">
    <title>400 - Bad Request</title>
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
                <h1 class="text-center">400 - Bad Request</h1>
                <p class="text-center">요청을 처리할 수 없습니다. 입력값을 확인해 주세요.</p>
                <c:if test="${not empty errorMessage}">
                    <p class="text-center text-danger">${errorMessage}</p>
                </c:if>
                <div class="text-center">
                    <a href="javascript:history.back()" class="btn btn-primary">이전 페이지로 돌아가기</a>
                    <a href="<c:url value='/'/>" class="btn btn-secondary">홈으로 돌아가기</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="<c:url value='/js/jquery-2.2.0.min.js'/>"></script>
<script src="<c:url value='/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/js/scripts.js'/>"></script>
</body>
</html>