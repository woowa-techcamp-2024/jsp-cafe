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

    <p class="text-gray-600 mb-4">전체 글 ${articles.size()}개</p>

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

    <div class="flex justify-center mt-6">
        <nav class="inline-flex rounded-md shadow">
            <a href="#" class="px-3 py-2 rounded-l-md border border-gray-300 bg-white text-gray-500 hover:bg-gray-50">&lt;</a>
            <a href="#" class="px-3 py-2 border-t border-b border-gray-300 bg-white text-teal-600 hover:bg-gray-50">1</a>
            <a href="#" class="px-3 py-2 border-t border-b border-gray-300 bg-white text-gray-500 hover:bg-gray-50">2</a>
            <a href="#" class="px-3 py-2 border-t border-b border-gray-300 bg-white text-gray-500 hover:bg-gray-50">3</a>
            <a href="#" class="px-3 py-2 border-t border-b border-gray-300 bg-white text-gray-500 hover:bg-gray-50">4</a>
            <a href="#" class="px-3 py-2 border-t border-b border-gray-300 bg-white text-gray-500 hover:bg-gray-50">5</a>
            <a href="#" class="px-3 py-2 rounded-r-md border border-gray-300 bg-white text-gray-500 hover:bg-gray-50">&gt;</a>
        </nav>
    </div>
</main>
</body>
</html>
