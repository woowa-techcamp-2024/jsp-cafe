<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="../common/header.jsp" />

<div class="login-container">
    <h2>로그인</h2>
    <form name="login" action="${pageContext.request.contextPath}/users/edit/${user.id}" method="post" class="login-form">
        <input type="email" id="email" name="email" value="${user.email}" readonly>
        <input type="nickname" id="newNickname" name="newNickname" value="${user.nickname}" placeholder="새로운 닉네임을 입력해주세요" required>
        <input type="password" id="newPassword" name="newPassword" placeholder="새로운 비밀번호를 입력해주세요" required>
        <input type="password" id="password" name="password" placeholder="기존 비밀번호를 입력해주세요" required>
        <button type="submit">로그인</button>
    </form>
</div>

<jsp:include page="../common/footer.jsp"/>