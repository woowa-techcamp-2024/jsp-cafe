<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>가입 완료</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 h-screen flex flex-col">
<header class="bg-white shadow">
    <div class="container mx-auto px-4 py-6 flex justify-between items-center">
        <h1 class="text-xl font-bold"><a href="/">HELLO, WEB!</a></h1>
        <a href="#" class="bg-teal-500 hover:bg-teal-600 text-white font-bold py-2 px-4 rounded">
            로그인/회원가입
        </a>
    </div>
</header>

<main class="flex-grow flex items-center justify-center">
    <div class="w-full max-w-md">
        <h2 class="text-3xl font-bold text-center mb-8">가입이 완료되었습니다!</h2>

        <div class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="email">
                    이메일
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline bg-gray-100" id="email" type="email" value="abc@codesquad.kr" readonly>
            </div>
            <div class="mb-6">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="nickname">
                    닉네임
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline bg-gray-100" id="nickname" type="text" value="user" readonly>
            </div>
            <div class="flex items-center justify-between">
                <a href="login.html" class="bg-teal-500 hover:bg-teal-600 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full text-center">
                    지금 로그인하기
                </a>
            </div>
        </div>
    </div>
</main>
</body>
</html>