<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<!-- 프로필 -->
<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading"><h4>Profile</h4></div>
            <div class="panel-body">
                <div class="well well-sm">
                    <div class="media">
                        <a class="thumbnail pull-left" href="#">
                            <img class="media-object" src="../images/80-text.png">
                        </a>
                        <div class="media-body">
                            <h4 class="media-heading">${user.username}</h4>
                            <p>
                                <a href="#" class="btn btn-xs btn-default"><span
                                        class="glyphicon glyphicon-envelope"></span>&nbsp;${user.email}</a>
                            </p>
                            <c:choose>
                                <c:when test="${user.deleted}">
                                    <h4 class="media-heading">탈퇴한 유저입니다.</h4>
                                </c:when>
                                <c:when test="${not user.deleted and not empty userPrincipal.id and userPrincipal.id == user.id}">
                                    <button class="link-delete-article" onclick="httpGet('/me/edit')">수정</button>
                                    <button class="link-delete-article" onclick="httpDelete('/me')">탈퇴</button>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/include/footer.jsp" %>
