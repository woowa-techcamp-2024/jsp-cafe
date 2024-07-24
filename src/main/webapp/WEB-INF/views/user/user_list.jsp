<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>사용자 목록</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen">

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<main class="container mx-auto mt-8 px-4">
    <h2 class="text-2xl font-bold mb-6">사용자 목록</h2>
    <div class="bg-white shadow rounded-lg overflow-hidden">
        <table class="w-full">
            <thead>
            <tr class="bg-gray-50 text-gray-600 uppercase text-sm leading-normal">
                <th class="py-3 px-6 text-left">닉네임</th>
                <th class="py-3 px-6 text-left">이메일</th>
            </tr>
            </thead>
            <tbody class="text-gray-600 text-sm font-light">
            <c:forEach var="user" items="${users}">
                <tr class="border-b border-gray-200 hover:bg-gray-100 cursor-pointer"
                    onclick="window.location.href='<c:url value="/users/${user.id()}"/>';">
                    <td class="py-3 px-6 text-left whitespace-nowrap">${user.nickname()}</td>
                    <td class="py-3 px-6 text-left">${user.email()}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="flex justify-center mt-6">
        <nav class="inline-flex rounded-md shadow">
            <a href="#"
               class="px-3 py-2 rounded-l-md border border-gray-300 bg-white text-gray-500 hover:bg-gray-50">&lt;</a>
            <a href="#"
               class="px-3 py-2 border-t border-b border-gray-300 bg-white text-teal-600 hover:bg-gray-50">1</a>
            <a href="#"
               class="px-3 py-2 border-t border-b border-gray-300 bg-white text-gray-500 hover:bg-gray-50">2</a>
            <a href="#"
               class="px-3 py-2 border-t border-b border-gray-300 bg-white text-gray-500 hover:bg-gray-50">3</a>
            <a href="#"
               class="px-3 py-2 rounded-r-md border border-gray-300 bg-white text-gray-500 hover:bg-gray-50">&gt;</a>
        </nav>
    </div>
</main>
</body>
</html>
