<%--<%@ page language="java" contentType="text/html; charset=UTF-8" %>--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/include/header.jsp" %>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <c:if test="${not empty error}">
                <script type="text/javascript">
                    alert("${error}");
                </script>
            </c:if>
            <form name="userModify" method="post" action="/users/edit">
                <input type="hidden" name="userId" value="${user.id}">
                <input type="hidden" name="username" value="${user.username}">
                <div class="form-group">
                    <label for="userId">사용자 아이디</label>
                    <input type="text" class="form-control" id="userId" name="username" placeholder="User ID"
                           value="${user.username}" readonly>
                </div>
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                </div>
                <div class="form-group">
                    <label for="confirmPassword">비밀번호 확인</label>
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
