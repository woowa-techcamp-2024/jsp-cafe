<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<html>

<%@ include file="common/header.jsp" %>

<body>

<%@ include file="common/navigation.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">
                <c:forEach var="articleResponse" items="${articles}">
                    <li>
                        <div class="wrap">
                            <div class="main">
                                <strong class="subject">
                                    <a href="${pageContext.request.contextPath}/articles/${articleResponse.articleId}">${articleResponse.title}</a>
                                </strong>
                                <div class="auth-info">
                                    <i class="icon-add-comment"></i>
                                    <span class="time">${articleResponse.createdAt}</span>
                                    <c:choose>
                                        <c:when test="${articleResponse.authorId != null}">
                                            <a href="${pageContext.request.contextPath}/users/${articleResponse.authorId}" class="author">${articleResponse.authorNickname}</a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="author">${articleResponse.authorNickname}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="reply" title="댓글">
                                    <i class="icon-reply"></i>
                                    <span class="point">${articleResponse.hits}</span>
                                </div>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6 text-center">
                    <ul class="pagination center-block" style="display:inline-block;">
                        <c:if test="${currentPage > 1}">
                            <li><a href="${pageContext.request.contextPath}/articles?page=${currentPage - 1}">&laquo;</a></li>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <li class="active"><a href="#">${i}</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li><a href="${pageContext.request.contextPath}/articles?page=${i}">${i}</a></li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <li><a href="${pageContext.request.contextPath}/articles?page=${currentPage + 1}">&raquo;</a></li>
                        </c:if>
                    </ul>
                </div>
                <div class="col-md-3 qna-write">
                    <a href="${pageContext.request.contextPath}/articles/write" class="btn btn-primary pull-right" role="button">질문하기</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<%@ include file="common/footer.jsp" %>

</body>
</html>