<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%
    // Retrieve the userId from the cookies
    String userId = null;
    if (request.getCookies() != null) {
        for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
            if ("WOOWA_SESSIONID".equals(cookie.getName())) {
                userId = cookie.getValue();
                break;
            }
        }
    }
%>

<nav>
    <div class="navbar-right">
        <c:choose>
            <c:when test="${empty sessionScope.WOOWA_SESSIONID}">
                <a href="${pageContext.request.contextPath}/users/login">로그인</a>
                <a href="${pageContext.request.contextPath}/users/registration">회원가입</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/users">멤버리스트</a>
                <a href="${pageContext.request.contextPath}/users/<%= userId %>">마이페이지</a>
                <form action="${pageContext.request.contextPath}/users/logout" method="post">
                    <button type="submit">로그아웃</button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</nav>