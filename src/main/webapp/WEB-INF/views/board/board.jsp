<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판 목록</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen">

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<main class="container mx-auto mt-8 px-4">
    <div class="bg-teal-500 text-white rounded-lg p-6 mb-8">
        <p class="text-sm mb-2">부스트캠프 백엔드 교육용 페이지</p>
        <h2 class="text-2xl font-bold">HELLO, WEB! 입니다.</h2>
    </div>

    <p class="text-gray-600 mb-4">전체 글 ${totalArticles}개</p>

    <div class="bg-white shadow rounded-lg overflow-hidden">
        <table class="w-full">
            <thead>
            <tr class="bg-gray-50 text-gray-600 uppercase text-sm leading-normal">
                <th class="py-3 px-6 text-left">제목</th>
                <th class="py-3 px-6 text-left">작성자</th>
                <th class="py-3 px-6 text-left">작성일자</th>
            </tr>
            </thead>
            <tbody class="text-gray-600 text-sm font-light">
            <c:forEach var="article" items="${articles}">
                <tr class="border-b border-gray-200 hover:bg-gray-100"
                    onclick="window.location.href='<c:url value="/articles/${article.id()}"/>';">
                    <td class="py-3 px-6 text-left whitespace-nowrap">${article.title()}</td>
                    <td class="py-3 px-6 text-left">${article.nickname()}</td>
                    <td class="py-3 px-6 text-left">
                            ${fn:substring(article.createAt(), 0, 4)}. ${fn:substring(article.createAt(), 5, 7)}. ${fn:substring(article.createAt(), 8, 10)}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <c:set var="pageSize" value="5" />

    <c:choose>
        <c:when test="${totalPages <= pageSize}">
            <c:set var="beginPage" value="1" />
            <c:set var="endPage" value="${totalPages}" />
        </c:when>
        <c:otherwise>
            <c:set var="beginPage" value="${currentPage - 2}" />
            <c:set var="endPage" value="${beginPage + 4}" />

            <c:if test="${beginPage < 1}">
                <c:set var="beginPage" value="1" />
                <c:set var="endPage" value="${beginPage + 4}" />
            </c:if>

            <c:if test="${endPage > totalPages}">
                <c:set var="endPage" value="${totalPages}" />
                <c:set var="beginPage" value="${endPage - 4}" />
            </c:if>

            <c:if test="${beginPage < 1}">
                <c:set var="beginPage" value="1" />
            </c:if>
        </c:otherwise>
    </c:choose>

    <div class="flex justify-center mt-6">
        <nav class="inline-flex rounded-md shadow">
            <c:if test="${currentPage > 1}">
                <a href="?page=${currentPage - 1}"
                   class="px-3 py-2 rounded-l-md border border-gray-300 bg-white text-gray-500 hover:bg-gray-50">&lt;</a>
            </c:if>
            <c:if test="${currentPage == 1}">
                <span class="px-3 py-2 rounded-l-md border border-gray-300 bg-gray-100 text-gray-400">&lt;</span>
            </c:if>

            <c:forEach begin="${beginPage}" end="${endPage}" var="i">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <span class="px-3 py-2 border-t border-b border-gray-300 bg-teal-600 text-white">${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="?page=${i}"
                           class="px-3 py-2 border-t border-b border-gray-300 bg-white text-gray-500 hover:bg-gray-50">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="?page=${currentPage + 1}"
                   class="px-3 py-2 rounded-r-md border border-gray-300 bg-white text-gray-500 hover:bg-gray-50">&gt;</a>
            </c:if>
            <c:if test="${currentPage == totalPages}">
                <span class="px-3 py-2 rounded-r-md border border-gray-300 bg-gray-100 text-gray-400">&gt;</span>
            </c:if>
        </nav>
    </div>

    <div class="fixed bottom-8 right-8 z-50">
        <a href="<c:url value='/articles/form'/>"
           class="bg-teal-500 hover:bg-teal-600 text-white font-bold py-2 px-4 rounded-full shadow-lg transition duration-300 ease-in-out transform hover:scale-105">
            게시글 작성
        </a>
    </div>
</main>
</body>
</html>
