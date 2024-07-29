<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>에러페이지</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .error-container {
            text-align: center;
            background-color: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .status_code {
            color: #e74c3c;
            font-size: 48px;
            margin-bottom: 20px;
        }

        .error_message {
            color: #34495e;
            font-size: 18px;
            margin-bottom: 30px;
        }

        .btn {
            display: inline-block;
            padding: 10px 20px;
            background-color: #3498db;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            transition: background-color 0.3s;
        }

        .btn:hover {
            background-color: #2980b9;
        }

    </style>
</head>
<body>
<div class="error-container">
    <h1 class="status_code">${errorResponse.statusCode}</h1>
    <p class="error_message">${errorResponse.message}</p>
    <a href="/" class="btn">홈으로 가기</a>
</div>
</body>
</html>
