<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<%@include file="/WEB-INF/share/header.jsp" %>
<body>
<%@include file="/WEB-INF/share/navbar.jsp" %>
<%@include file="/WEB-INF/share/sub_navbar.jsp" %>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <c:choose>
                <c:when test="${checkPassword}">
                    <c:if test="${isFailed}">
                        <div class="alert alert-danger" role="alert">비밀번호를 확인하세요.</div>
                    </c:if>
                    <form name="question" method="post" action="/users/profile">
                        <div class="form-group">
                            <label for="name">이름</label>
                            <input class="form-control" id="name" name="name" placeholder="Name"
                                   value="<c:out value="${loginUser.name}" />">
                        </div>
                        <div class="form-group">
                            <label for="email">이메일</label>
                            <input type="email" class="form-control" id="email" name="email" placeholder="Email"
                                   value="<c:out value="${loginUser.email}" />">
                        </div>
                        <button type="submit" class="btn btn-success clearfix pull-right">정보 수정</button>
                        <div class="clearfix"/>
                    </form>
                </c:when>
                <c:otherwise>
                    <form name="question" method="post" action="/users/profile">
                        <div class="form-group">
                            <label for="password">비밀번호</label>
                            <input type="password" class="form-control" id="password" name="password"
                                   placeholder="Password">
                        </div>
                        <button type="submit" class="btn btn-success clearfix pull-right">비밀번호 확인</button>
                        <div class="clearfix"/>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!-- script references -->
<script src="../../js/jquery-2.2.0.min.js"></script>
<script src="../../js/bootstrap.min.js"></script>
<script src="../../js/scripts.js"></script>
</body>
</html>