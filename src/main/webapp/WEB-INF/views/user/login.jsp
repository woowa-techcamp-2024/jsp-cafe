<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko" class="h-full bg-gray-100">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="h-full flex flex-col">

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<main class="flex-grow flex items-center justify-center">
    <div class="w-full max-w-md">
        <h2 class="text-3xl font-bold text-center mb-8">로그인</h2>

        <c:if test="${loginFailed}">
            <div id="passwordError"
                 class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4"
                 role="alert">
                <strong class="font-bold">오류!</strong>
                <span class="block sm:inline">현재 비밀번호가 일치하지 않습니다.</span>
            </div>
        </c:if>

        <form class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4" method="post">
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="email">
                    이메일
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                       id="email" name="email" type="email" placeholder="이메일을 입력해주세요">
            </div>
            <div class="mb-6">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="password">
                    비밀번호
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
                       id="password" name="password" type="password" placeholder="비밀번호를 입력해주세요">
            </div>
            <div class="flex items-center justify-between mb-6">
                <button class="bg-teal-500 hover:bg-teal-600 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full"
                        type="submit">
                    로그인
                </button>
            </div>
            <div class="text-center">
                <span class="text-gray-600">아직 회원가입을 안하셨나요? </span>
                <a href="/users/sign" class="text-teal-500 hover:text-teal-600">회원가입하기</a>
            </div>
        </form>
    </div>
</main>
</body>
</html>
