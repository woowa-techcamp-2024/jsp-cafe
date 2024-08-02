<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

                <c:forEach var="article" items="${questions}" varStatus="status">
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
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6 text-center">
<%--                    <ul class="pagination center-block" style="display:inline-block;">--%>
<%--                        <li><a href="#">«</a></li>--%>
<%--                        <li><a href="#">1</a></li>--%>
<%--                        <li><a href="#">2</a></li>--%>
<%--                        <li><a href="#">3</a></li>--%>
<%--                        <li><a href="#">4</a></li>--%>
<%--                        <li><a href="#">5</a></li>--%>
<%--                        <li><a href="#">»</a></li>--%>
<%--                    </ul>--%>
                </div>
                <div class="col-md-3 qna-write">
                    <a href="${pageContext.request.contextPath}/questions/form" class="btn btn-primary pull-right" role="button">질문하기</a>
                </div>
            </div>
        </div>
    </div>
</div>

<div>
    <jsp:include page="/template/component/script.jsp"></jsp:include>
</div>
</body>
</html>