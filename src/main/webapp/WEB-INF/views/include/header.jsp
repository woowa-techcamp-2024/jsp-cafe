<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>jsp-cafe</title>

    <link href="/static/favicon.ico" rel="icon" type="image/x-icon">
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="/static/css/styles.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-fixed-top header">
    <div class="col-md-12">
        <div class="navbar-header">

            <a href="/" class="navbar-brand">JSP-CAFE</a>
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse1">
                <i class="glyphicon glyphicon-search"></i>
            </button>

        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse1">
            <form class="navbar-form pull-left">
                <div class="input-group" style="max-width:470px;">
                    <input type="text" class="form-control" placeholder="Search" name="srch-term" id="srch-term">
                    <div class="input-group-btn">
                        <button class="btn btn-default btn-primary" type="submit"><i
                                class="glyphicon glyphicon-search"></i></button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</nav>
<div class="navbar navbar-default" id="subnav">
    <div class="col-md-12">
        <div class="navbar-header">
            <a href="#" style="margin-left:15px;" class="navbar-btn btn btn-default btn-plus dropdown-toggle"
               data-toggle="dropdown"><i class="glyphicon glyphicon-home" style="color:#dd1111;"></i> Home <small><i
                    class="glyphicon glyphicon-chevron-down"></i></small></a>
            <ul class="nav dropdown-menu">
                <li>
                    <a href="/users/list">
                        <i class="glyphicon glyphicon-user" style="color:#1111dd;"></i>
                        사용자 목록
                    </a>
                </li>

                <c:if test="${not empty sessionScope.userPrincipal}">
                    <li>
                        <a href="/users/${sessionScope.userPrincipal.id}">
                            <i class="glyphicon glyphicon-user" style="color:#1111dd;"></i>
                            Profile
                        </a>
                    </li>
                </c:if>
            </ul>

            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse2">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>

        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse2">
            <ul class="nav navbar-nav navbar-right">
                <li class="active"><a href="/">Posts</a></li>
                <c:choose>
                    <c:when test="${empty sessionScope.userPrincipal}">
                        <li><a href="/login" role="button">로그인</a></li>
                        <li><a href="/users/join" role="button">회원가입</a></li>
                    </c:when>
                    <c:when test="${not empty sessionScope.userPrincipal}">
                        <li><a href="/logout" role="button">로그아웃</a></li>
                        <li><a href="/users/edit" role="button">개인정보수정</a></li>
                    </c:when>
                </c:choose>
            </ul>
        </div>
    </div>
</div>
