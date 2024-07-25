<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/base/head.jsp" %>
<%@ include file="/WEB-INF/base/header.jsp" %>
<%@ include file="/WEB-INF/base/nav.jsp" %>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <form name="question" method="post" action="/users/${user.userId}/form">
                <div class="form-group">
                    <label for="userId">사용자 아이디</label>
                    <input class="form-control" id="userId" placeholder="User ID" readonly value=${user.userId}>
                </div>
                <div class="form-group">
                    <label for="current_password">현재 비밀번호</label>
                    <input type="password" class="form-control" id="current_password" name="checkPassword"
                           placeholder="Password">
                </div>
                <div class="form-group">
                    <label for="password">변경할 비밀번호</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password"
                           value=${user.password}>
                </div>
                <div class="form-group">
                    <label for="name">이름</label>
                    <input class="form-control" id="name" name="nickname" placeholder="Name" value=${user.nickname}>
                </div>
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="Email"
                           value=${user.email}>
                </div>

                <button type="submit" class="btn btn-success clearfix pull-right">수정하기</button>
                <div class="clearfix"/>
            </form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/base/footer.jsp" %>