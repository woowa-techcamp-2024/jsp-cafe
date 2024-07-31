<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Posting</title>
    <link rel="stylesheet" href="/css/common.css">
</head>
<body>
    <div class="container">
        <%@ include file="/common/header.jsp" %>
        <main>
            <h2>글쓰기</h2>
            <form action="/api/articles" method="post">
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" id="title" name="title" placeholder="글의 제목을 입력하세요">
                </div>
                <div class="form-group">
                    <label for="content">내용</label>
                    <textarea id="content" name="content" placeholder="글의 내용을 입력하세요"></textarea>
                </div>
                <button type="submit" class="btn">작성 완료</button>
            </form>
        </main>
    </div>
</body>
</html>