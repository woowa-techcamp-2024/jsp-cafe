<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="../common/header.jsp"/>

<!-- 새로운 CSS 파일 링크 추가 -->

<div class="write-article-container">
    <h1>글쓰기</h1>
    <form name="article" method="post" action="${pageContext.request.contextPath}/articles/write" class="write-article-form">
        <input type="text" id="title" name="title" placeholder="글의 제목을 입력하세요"/>
        <textarea name="content" id="content" placeholder="글의 내용을 입력하세요"></textarea>
        <button type="submit">작성 완료</button>
    </form>
</div>

<%@ include file="../common/footer.jsp" %>
</body>
</html>