<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--<%@ taglib prefix="c" uri="jakarta.tags.core" %>--%>

<html>

<%@ include file="../common/header.jsp" %>

<body>

<%@ include file="../common/navigation.jsp" %>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">로그인 페이지</h3>
                <h2>로그인 해주세요.</h2>
            </div>
            <div class="panel-body">
                <form name="login" method="post" action="${pageContext.request.contextPath}/users/login">
                    <div class="form-group">
                        <label for="email">이메일</label>
                        <input class="form-control" id="email" name="email" placeholder="Email" required>
                    </div>
                    <div class="form-group">
                        <label for="password">비밀번호</label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
                    </div>
                    <button type="submit" class="btn btn-success clearfix pull-right">로그인</button>
                    <div class="clearfix"></div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>

</body>
</html>
