<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="../common/header.jsp"/>


<div class="login-container">
    <h2>로그인</h2>
    <form name="login" action="${pageContext.request.contextPath}/users/registration" method="post" class="login-form">
        <input type="email" id="email" name="email" placeholder="이메일을 입력해주세요" required>
        <input type="nickname" id="nickname" name="nickname" placeholder="닉네임을 입력해주세요" required>
        <input type="password" id="password" name="password" placeholder="비밀번호를 입력해주세요" required>
        <button type="submit">회원가입</button>
    </form>
</div>

<jsp:include page="../common/footer.jsp"/>