<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/base/head.jsp" %>
<%@ include file="/WEB-INF/base/header.jsp" %>
<%@ include file="/WEB-INF/base/nav.jsp" %>

<div class="container" id="main">
    <div class="error-section">
        <h3>오류 발생: ${errorMessage} </h3>
        <p>다음 링크를 통해 다른 페이지로 이동하세요:</p>
        <a href="/">홈으로 가기</a>
    </div>
</div>

<%@ include file="/WEB-INF/base/footer.jsp" %>
