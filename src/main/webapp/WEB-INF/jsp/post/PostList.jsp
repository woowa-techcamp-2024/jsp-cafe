<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="<c:url value='/static/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/static/css/styles.css'/>" rel="stylesheet">
</head>
<body>
<%@ include file="../Header.jsp"%>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">
                <c:forEach items="${pagedResult.posts}" var="post">
                    <li>
                        <div class="wrap">
                            <div class="main">
                                <strong class="subject">
                                    <a href="questions/${post.id}">${post.title}</a>
                                </strong>
                                <div class="auth-info">
                                    <i class="icon-add-comment"></i>
                                    <span class="time">
                                            ${post.formattedCreatedAt}
                                    </span>
                                    <a href="#" class="author">${post.username}</a>
                                </div>
                                <div class="reply" title="댓글">
                                    <i class="icon-reply"></i>
                                    <span class="point">0</span>
                                </div>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6 text-center">
                    <nav aria-label="Page navigation">
                        <ul class="pagination">
                            <c:if test="${hasPreviousGroup}">
                                <li>
                                    <a href="?page=${startPage - 1}" aria-label="Previous Group">
                                        <span aria-hidden="true">&laquo;</span>
                                    </a>
                                </li>
                            </c:if>

                            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                <li class="${i == pagedResult.currentPage ? 'active' : ''}">
                                    <a href="?page=${i}">${i}</a>
                                </li>
                            </c:forEach>

                            <c:if test="${hasNextGroup}">
                                <li>
                                    <a href="?page=${endPage + 1}" aria-label="Next Group">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                </div>
                <div class="col-md-3 qna-write">
                    <a href="/questions" class="btn btn-primary pull-right" role="button">질문하기</a>
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