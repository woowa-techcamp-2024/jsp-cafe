<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="WEB-INF/template/head.jsp"%>
<body>
<%@ include file="WEB-INF/template/top-header.jsp"%>
<%@ include file="WEB-INF/template/sub-header.jsp"%>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <form name="question" method="POST" action="/users">
                <div class="form-group">
                    <label for="id">사용자 아이디</label>
                    <input class="form-control" id="id" name="id" placeholder="User ID">
                </div>
                <div class="form-group">
                    <label for="password">비밀번호 (영문 + 숫자, 8자 이상 16자 이하)</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                </div>
                <div class="form-group">
                    <label for="name">이름</label>
                    <input class="form-control" id="name" name="name" placeholder="Name">
                </div>
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="Email">
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">회원가입</button>
                <div class="clearfix" />
            </form>
        </div>
    </div>
</div>

<!-- script references -->
<%@include file="WEB-INF/template/footer.jsp" %>
</body>
</html>