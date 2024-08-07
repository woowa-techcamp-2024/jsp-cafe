<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="/template/component/head.jsp"></jsp:include>

<body>
<div>
    <jsp:include page="/template/component/nav.jsp"></jsp:include>
</div>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">

                <c:forEach var="article" items="${questions.content}" varStatus="status">
                <li>
                    <div class="wrap">
                        <div class="main">
                            <strong class="subject">
                                <a href="${pageContext.request.contextPath}/questions/${article.id}"><c:out value="${article.title}"/></a>
                            </strong>
                            <div class="auth-info">
                                <i class="icon-add-comment"></i>
                                <span class="time">${article.createdAt}</span>
                                <a href="${pageContext.request.contextPath}/user/profile.jsp" class="author"><c:out value="${article.writer}"/></a>
                            </div>
                            <div class="reply" title="댓글">
                                <i class="icon-reply"></i>
                                <span class="point">🏕️</span>
                            </div>
                        </div>
                    </div>
                </li>
                </c:forEach>

            </ul>

            <!-- 하단 페이지 번호 및 글쓰기 버튼 -->
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6 text-center">

                    <ul class="pagination center-block" style="display:inline-block;">
                        <c:set var="pageGroup" value="${fn:substringBefore((questions.numberOfPage - 1) / 5, '.')}"/>
                        <c:set var="startPage" value="${pageGroup * 5 + 1}"/>
                        <c:set var="endPage" value="${startPage + 4}"/>

                        <c:if test="${endPage > questions.numberOfEnd}">
                            <c:set var="endPage" value="${questions.numberOfEnd}"/>
                        </c:if>

                        <c:if test="${pageGroup > 0}">
                            <c:set var="startPageInt" value="${startPage}"/>
                            <li><a href="${pageContext.request.contextPath}?page=${startPageInt - 1}">«</a></li>
                        </c:if>

                        <c:forEach begin="${startPage}" end="${endPage}" var="i">
                            <li <c:if test="${questions.numberOfPage == i}">class="active"</c:if>>
                                <a href="${pageContext.request.contextPath}?page=${i}">${i}</a>
                            </li>
                        </c:forEach>

                        <c:if test="${endPage < questions.numberOfEnd}">
                            <c:set var="startPageInt" value="${startPage}"/>
                            <c:set var="nextPageInt" value="${startPageInt + 5}"/>

                            <li><a href="${pageContext.request.contextPath}?page=${nextPageInt}">»</a></li>
                        </c:if>
                    </ul>

                </div>
                <div class="col-md-3 qna-write">
                    <a href="${pageContext.request.contextPath}/questions/form" class="btn btn-primary pull-right" role="button">질문하기</a>
                </div>
            </div>

        </div>
    </div>
</div>

<script>
    console.log("현재페이지: ${questions.numberOfPage}");
    console.log("마지막페이지: ${questions.numberOfEnd}");
</script>
<div>
    <jsp:include page="/template/component/script.jsp"></jsp:include>
</div>
</body>
</html>