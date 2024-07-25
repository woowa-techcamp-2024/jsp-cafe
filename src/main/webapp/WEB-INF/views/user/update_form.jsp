<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

<%@ include file="../common/header.jsp" %>

<body>

<%@ include file="../common/navigation.jsp" %>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <form name="question" action="${pageContext.request.contextPath}/users/edit/${user.id}" method="post">
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email" class="form-control" id="email" name="email" value="${user.email}" readonly>
                </div>
                <div class="form-group">
                    <label for="nickname">새로운 닉네임</label>
                    <input class="form-control" id="nickname" name="nickname" value="${user.nickname}" placeholder="Name">
                </div>
                <div class="form-group">
                    <label for="password">새로운 비밀번호</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="New Password">
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">정보 수정</button>
                <div class="clearfix" />
            </form>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>

</body>
</html>