<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header class="header">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <h1 class="header-title"><a href="${pageContext.request.contextPath}/">HELLO, WEB!</a></h1>
    <nav>
        <c:choose>
            <c:when test="${sessionScope.isLogined}">
                <span class="user-name">환영합니다, ${sessionScope.nickname}!</span>
                <form action="${pageContext.request.contextPath}/api/logout" method="post" style="display: inline;">
                    <button type="submit" class="logout-button">로그아웃</button>
                </form>
                <form action="${pageContext.request.contextPath}/edit-profile.jsp" method="get" style="display: inline;">
                    <button type="submit" class="edit-profile-button">정보수정</button>
                </form>
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/login" method="get" style="display: inline;">
                    <button type="submit" class="login-button">로그인</button>
                </form>
                <form action="${pageContext.request.contextPath}/signup" method="get" style="display: inline;">
                    <button type="submit" class="signup-button">회원가입</button>
                </form>
            </c:otherwise>
        </c:choose>
        <form action="${pageContext.request.contextPath}/users" method="get" style="display: inline;">
            <button type="submit" class="user-list-button">사용자 목록</button>
        </form>
    </nav>
</header>
