<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 상세</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen">

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<main class="container mx-auto mt-8 px-4">
    <div class="bg-white shadow rounded-lg p-6">
        <h2 class="text-2xl font-bold mb-4">${article.title()}</h2>
        <div class="mb-4 text-sm text-gray-600">
            <span>작성자: ${article.nickname()}</span> |
            <span>작성일: ${fn:substring(article.createAt(), 0, 4)}년 ${fn:substring(article.createAt(), 5, 7)}월 ${fn:substring(article.createAt(), 8, 10)}일</span>
        </div>
        <div class="border-t border-b py-4 mb-4">
            <p>${article.content()}</p>
        </div>
        <div class="flex justify-end space-x-2">
            <a href="<c:url value="/articles/${article.id()}/form"/>" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded">수정</a>
            <a href="#" class="bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded">삭제</a>
            <a href="/" class="bg-gray-500 hover:bg-gray-600 text-white font-bold py-2 px-4 rounded">목록</a>
        </div>
    </div>
</main>
</body>
</html>
