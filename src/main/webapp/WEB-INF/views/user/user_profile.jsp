<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>프로필</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 h-screen flex flex-col">

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<main class="flex-grow flex items-center justify-center">
    <div class="w-full max-w-md">
        <h2 class="text-3xl font-bold text-center mb-8">프로필</h2>
        <form class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="email">
                    이메일
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                       id="email" name="email" type="email" value="<c:out value='${user.email()}'/>"
                       readonly>
            </div>
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="nickname">
                    닉네임
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                       id="nickname" name="nickname" type="text"
                       value="<c:out value='${user.nickname()}'/>" readonly>
            </div>
            <div class="flex justify-end">
                <a href="<c:url value='/users/${user.id()}/form'/>"
                   class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                    회원정보 수정
                </a>
            </div>
        </form>
    </div>
</main>
</body>
</html>
