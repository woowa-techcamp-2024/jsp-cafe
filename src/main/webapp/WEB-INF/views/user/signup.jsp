<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 h-screen flex flex-col">

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<main class="flex-grow flex items-center justify-center">
    <div class="w-full max-w-md">
        <h2 class="text-3xl font-bold text-center mb-8">회원가입</h2>

        <form class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4" method="post">
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="email">
                    이메일
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                       id="email" name="email" type="email" placeholder="이메일을 입력해주세요">
            </div>
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="nickname">
                    닉네임
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                       id="nickname" name="nickname" type="text" placeholder="닉네임을 입력해주세요">
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
                    회원가입
                </button>
            </div>
            <div class="text-center">
                <span class="text-gray-600">이미 회원이신가요? </span>
                <a href="/login.jsp" class="text-teal-500 hover:text-teal-600">로그인하기</a>
            </div>
        </form>
    </div>
</main>
</body>
</html>
