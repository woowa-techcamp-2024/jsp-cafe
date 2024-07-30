<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="../common/header.jsp"/>

<%--<body class="login-body">--%>
<div class="login-container">
    <h2>로그인</h2>
    <form name="login" action="${pageContext.request.contextPath}/users/login" method="post" class="login-form">
        <%--            <label for="email">이메일</label>--%>
        <input type="email" id="email" name="email" placeholder="이메일을 입력해주세요" required>
        <%--            <label for="password">비밀번호</label>--%>
        <input type="password" id="password" name="password" placeholder="비밀번호를 입력해주세요" required>
        <button type="submit">로그인</button>
    </form>
    <p>아직 회원가입을 안하셨나요? <a href="${pageContext.request.contextPath}/users/registration">회원가입하기</a></p>
</div>
<%--</body>--%>

<jsp:include page="../common/footer.jsp"/>