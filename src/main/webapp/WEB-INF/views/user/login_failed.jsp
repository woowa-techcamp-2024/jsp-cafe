<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>

<%@ include file="../common/header.jsp" %>

<body>

<%@ include file="../common/navigation.jsp" %>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <div class="alert alert-danger" role="alert">아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요.</div>
            <form name="question" method="post" action="${pageContext.request.contextPath}/users/login">
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input class="form-control" id="email" name="email" placeholder="Email">
                </div>
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">로그인</button>
                <div class="clearfix" />
            </form>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>

</body>
</html>