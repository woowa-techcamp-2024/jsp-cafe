<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<nav>
    <div class="navbar-right">
    <c:choose>
        <c:when test="${empty sessionScope.WOOWA_SESSIONID}">
            <a href="${pageContext.request.contextPath}/users/login">로그인</a>
            <a href="${pageContext.request.contextPath}/users/registration">회원가입</a>
        </c:when>
        <c:otherwise>
            <a href="${pageContext.request.contextPath}/users">멤버리스트</a>
            <a href="${pageContext.request.contextPath}/users/${sessionScope.WOOWA_SESSIONID}">마이페이지</a>
            <form action="${pageContext.request.contextPath}/users/logout" method="post" style="display:inline;">
                <button type="submit">로그아웃</button>
            </form>
        </c:otherwise>
    </c:choose>
    </div>
</nav>