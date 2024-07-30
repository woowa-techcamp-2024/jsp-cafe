<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>HELLO, 우테캠!</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/common.css">
</head>
<body>
<header>
    <div class="container">
        <div class="logo">
            <a href="${pageContext.request.contextPath}/">HELLO, 우테캠!</a>
        </div>
        <jsp:include page="../common/navigation.jsp" />
    </div>
</header>