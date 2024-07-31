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

<main class="flex-grow flex items-center justify-center bg-gray-100 p-4">
    <div class="w-full max-w-lg">
        <div class="bg-white shadow-2xl rounded-lg overflow-hidden">
            <div class="p-8">
                <div class="flex items-center justify-center w-20 h-20 mx-auto mb-6 bg-red-100 rounded-full">
                    <svg class="w-10 h-10 text-red-500" fill="none" stroke="currentColor"
                         viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                </div>
                <h2 class="text-5xl font-bold text-center text-red-500 mb-2">
                    <c:out value="${errorCode}"/>
                </h2>
                <h3 class="text-2xl font-semibold text-center text-gray-700 mb-6">오류가 발생했습니다</h3>
                <p class="text-center text-gray-600 mb-8">
                    <c:out value="${errorMessage}"/>
                </p>
                <div class="flex justify-center">
                    <a href="<c:url value='/'/>"
                       class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-3 px-6 rounded-lg transition duration-300 ease-in-out transform hover:-translate-y-1 hover:scale-110 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50">
                        홈으로 돌아가기
                    </a>
                </div>
            </div>
        </div>
    </div>
</main>

</body>
</html>
