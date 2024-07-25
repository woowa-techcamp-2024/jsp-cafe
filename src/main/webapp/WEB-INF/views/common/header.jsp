<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="bg-white shadow">
    <div class="container mx-auto px-4 py-6 flex justify-between items-center">
        <h1 class="text-xl font-bold"><a href="/">HELLO, WEB!</a></h1>
        <div class="flex items-center">
            <c:choose>
                <c:when test="${not empty userInfo}">
                    <span class="mr-4">안녕하세요, ${userInfo.nickname()}님!</span>
                    <a href="/users/logout" class="bg-teal-500 hover:bg-teal-600 text-white font-bold py-2 px-4 rounded mr-2">
                        로그아웃
                    </a>
                    <a href="<c:url value="/users/${userInfo.id()}/form"/>" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded">
                        회원정보 수정
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="/users/login" class="bg-teal-500 hover:bg-teal-600 text-white font-bold py-2 px-4 rounded">
                        로그인/회원가입
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>
