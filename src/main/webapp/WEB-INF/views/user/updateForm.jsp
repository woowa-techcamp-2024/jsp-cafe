<%@ page import="com.woowa.cafe.domain.Member" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="/WEB-INF/components/header.html" %>
<body>
<%@ include file="/WEB-INF/components/navbar-fixed-top-header.html" %>
<%@ include file="/WEB-INF/components/navbar-default.jsp" %>
<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <div class="panel-heading">
                <h3 class="panel-title">회원정보 수정</h3>
            </div>
            <form name="updateUser" method="post" action=<%="/user/" + session.getAttribute("memberId")%>>
                <div class="form-group">
                    <label for="memberId">사용자 아이디</label>
                    <input class="form-control" id="memberId" name="memberId"
                           placeholder="<%= ((Member)request.getAttribute("member")).getMemberId()%>"
                           readonly="readonly">
                </div>
                <div class="form-group">
                    <label for="lastPassword">이전 비밀번호</label>
                    <input type="password" class="form-control" id="lastPassword" name="lastPassword"
                           placeholder="lastPassword">
                </div>
                <div class="form-group">
                    <label for="newPassword">바꿀 비밀번호</label>
                    <input type="password" class="form-control" id="newPassword" name="newPassword"
                           placeholder="newPassword">
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
                <div class="clearfix"/>
            </form>
        </div>
    </div>
</div>
<!-- script references -->
<%@ include file="/WEB-INF/components/scirpt-refernces.html" %>
</body>
</html>
