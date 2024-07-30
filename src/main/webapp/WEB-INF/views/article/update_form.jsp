<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="../common/header.jsp"/>

<div class="write-article-container">
    <h1>글 수정</h1>
    <form name="article" method="post" action="${pageContext.request.contextPath}/articles/edit/${article.id}" class="write-article-form">
        <input type="hidden" name="_method" value="PUT"/>
        <input type="text" id="title" name="title" value="${article.title}" placeholder="글의 제목을 입력하세요"/>
        <textarea name="content" id="content" placeholder="글의 내용을 입력하세요">${article.content}</textarea>
        <button type="submit">수정 하기</button>
    </form>
</div>

<%@ include file="../common/footer.jsp" %>
</body>
</html>