<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>에러 페이지</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 h-screen flex flex-col">

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<main class="flex-grow flex items-center justify-center">
    <div class="w-full max-w-md">
        <h2 class="text-3xl font-bold text-center mb-8">오류가 발생했습니다</h2>
        <div class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2">
                    에러 코드
                </label>
                <p class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight">
                    <c:out value="${errorCode}"/>
                </p>
            </div>
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2">
                    에러 메시지
                </label>
                <p class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight">
                    <c:out value="${errorMessage}"/>
                </p>
            </div>
            <div class="flex justify-center">
                <a href="<c:url value='/'/>" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                    홈으로 돌아가기
                </a>
            </div>
        </div>
    </div>
</main>
</body>
</html>
