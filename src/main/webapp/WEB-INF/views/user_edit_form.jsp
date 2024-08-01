<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/include/header.jsp" %>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">${error}</div>
            </c:if>
            <form name="userModify" method="post" action="/users/edit">
                <input type="hidden" name="id" value="${user.id}">
                <input type="hidden" name="username" value="${user.username}">
                <div class="form-group">
                    <label for="originalPassword">기존 비밀번호</label>
                    <input type="password" class="form-control" id="originalPassword" name="originalPassword"
                           placeholder="Original Password">
                </div>
                <div class="form-group">
                    <label for="password">새로운 비밀번호</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                </div>
                <div class="form-group">
                    <label for="confirmPassword">새로운 비밀번호 확인</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword"
                           placeholder="Confirm Password">
                </div>
                <div class="form-group">
                    <label for="name">이름</label>
                    <input class="form-control" id="name" name="name" placeholder="Name" value="${user.name}">
                </div>
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="Email"
                           value="${user.email}">
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">수정하기</button>
                <div class="clearfix"/>
            </form>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/include/footer.jsp" %>