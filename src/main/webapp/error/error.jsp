<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error Page</title>
    <link rel="stylesheet" href="/css/common.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>HELLO, WEB!</h1>
            <div class="buttons">
                <a href="/"><button class="btn">홈으로</button></a>
            </div>
        </header>
        <div class="info-box">
            <h2>오류가 발생했습니다</h2>
            <p>죄송합니다. 요청을 처리하는 중 문제가 발생했습니다.</p>
        </div>
        <main>
            <%
                Integer statusCode = (Integer) request.getAttribute("status_code");
                String errorMessage = (String) request.getAttribute("message");

                if (statusCode == null) {
                    statusCode = response.getStatus();
                }
                if (errorMessage == null || errorMessage.isEmpty()) {
                    errorMessage = "알 수 없는 오류가 발생했습니다.";
                }
            %>

            <div class="form-group">
                <label>Status Code:</label>
                <input type="text" value="<%= statusCode %>" readonly>
            </div>

            <div class="form-group">
                <label>Error Message:</label>
                <textarea readonly><%= errorMessage %></textarea>
            </div>

            <button class="btn" onclick="window.history.back();">이전 페이지로 돌아가기</button>
        </main>
    </div>
</body>
</html>