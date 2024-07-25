<%@ page import="domain.Users" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="../header.jsp" %>
<body>
<%@ include file="../navigationbar.jsp" %>
<% Users user = (Users) request.getAttribute("user"); %>
<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <form name="question" method="post" action="/users/<%= user.getId() %>/form">
                <div class="form-group">
                    <label for="userId">사용자 아이디</label>
                    <input class="form-control" id="userId" name="userId" placeholder="User ID" value=<%= user.getUserId() %> readonly>
                </div>
                <div class="form-group">
                    <label for="password">기존 비밀번호</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                </div>
                <div class="form-group">
                    <label for="password">새 비밀번호</label>
                    <input type="password" class="form-control" id="newPassword" name="newPassword" placeholder="Password">
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
<%@ include file="../footer.jsp" %>
</body>
</html>