<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-fixed-top header">
    <div class="col-md-12">
        <div class="navbar-header">
            <a href="${pageContext.request.contextPath}" class="navbar-brand">JSP Cafe</a>
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse1">
                <i class="glyphicon glyphicon-search"></i>
            </button>
        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse1">
<%--            <form class="navbar-form pull-left">--%>
<%--                <div class="input-group" style="max-width:470px;">--%>
<%--                    <input type="text" class="form-control" placeholder="Search" name="srch-term" id="srch-term">--%>
<%--                    <div class="input-group-btn">--%>
<%--                        <button class="btn btn-default btn-primary" type="submit"><i class="glyphicon glyphicon-search"></i></button>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </form>--%>
            <ul class="nav navbar-nav navbar-right">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li>
                            <a href="#">
                                <i class="glyphicon glyphicon-user"></i>
                                <span>${sessionScope.user.userId}님, 환영합니다.</span>
                            </a>
                        </li>
                    </c:when>
                </c:choose>
                <li><a href="${pageContext.request.contextPath}/users"><i class="glyphicon glyphicon-th-list"></i>회원목록</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="navbar navbar-default" id="subnav">
    <div class="col-md-12">
        <div class="collapse navbar-collapse" id="navbar-collapse2">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="${pageContext.request.contextPath}">Q&A</a></li>
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <li><a href="${pageContext.request.contextPath}/auth/login.jsp" role="button">로그인</a></li>
                        <li><a href="${pageContext.request.contextPath}/user/form.jsp" role="button">회원가입</a></li>
                    </c:when>
                    <c:otherwise>
                        <li>
                            <a>
                            <form action="${pageContext.request.contextPath}/auth/logout" method="post" style="display:inline;">
                                <button type="submit" style="border:none; background:none; padding:0; margin:0; cursor:pointer; font:inherit;">
                                    로그아웃
                                </button>
                            </form>
                            </a>
                        </li>
                        <li><a href="${pageContext.request.contextPath}/users/${sessionScope.user.userId}/form" role="button">개인정보수정</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</div>

