<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko" class="h-full bg-gray-100">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="h-full flex flex-col">
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
        <form class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
            <div class="mb-4">
                <a href="/login.jsp">로그인 화면</a>
            </div>
            <div class="mb-4">
                <a href="/users/sign">회원가입 화면</a>
            </div>
            <div class="mb-4">
                <a href="/users">유저 리스트 화면</a>
            </div>
        </form>
    </div>
</main>
</body>
</html>
