<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="../common/header.jsp" />

<main>
    <h2>회원가입</h2>
    <form action="signup" method="post">
        <div>
            <label for="email">이메일</label>
            <input type="email" id="email" name="email" placeholder="이메일을 입력해주세요" required>
        </div>
        <div>
            <label for="nickname">닉네임</label>
            <input type="text" id="nickname" name="nickname" placeholder="닉네임을 입력해주세요" required>
        </div>
        <div>
            <label for="password">비밀번호</label>
            <input type="password" id="password" name="password" placeholder="비밀번호를 입력해주세요" required>
        </div>
        <button type="submit">회원가입</button>
    </form>
</main>

<jsp:include page="../common/footer.jsp" />