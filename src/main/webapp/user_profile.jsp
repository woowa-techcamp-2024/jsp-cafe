<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>사용자 프로필</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen">
<header class="bg-white shadow">
    <div class="container mx-auto px-4 py-6 flex justify-between items-center">
        <h1 class="text-xl font-bold"><a href="/">HELLO, WEB!</a></h1>
        <a href="#" class="bg-teal-500 hover:bg-teal-600 text-white font-bold py-2 px-4 rounded">
            로그인/회원가입
        </a>
    </div>
</header>

<main class="container mx-auto mt-8 px-4">
    <div class="bg-white shadow rounded-lg p-6">
        <h2 class="text-2xl font-bold mb-6">내 프로필</h2>
        <div class="space-y-4">
            <div>
                <label class="block text-sm font-medium text-gray-700">이메일</label>
                <p class="mt-1 block w-full px-3 py-2 bg-gray-100 border border-gray-300 rounded-md shadow-sm">
                    user@example.com
                </p>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700">닉네임</label>
                <p class="mt-1 block w-full px-3 py-2 bg-gray-100 border border-gray-300 rounded-md shadow-sm">
                    사용자닉네임
                </p>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700">가입일</label>
                <p class="mt-1 block w-full px-3 py-2 bg-gray-100 border border-gray-300 rounded-md shadow-sm">
                    2023년 7월 1일
                </p>
            </div>
        </div>
        <div class="mt-6">
            <a href="#" class="bg-teal-500 hover:bg-teal-600 text-white font-bold py-2 px-4 rounded">
                프로필 수정
            </a>
        </div>
    </div>
</main>
</body>
</html>
