<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<!-- 사용자 목록 -->

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">
                <li>
                    <div class="summary">전체 사용자 ${total}명</div>
                </li>
                <c:forEach var="user" items="${users}" varStatus="status">
                    <li>
                        <div class="wrap">
                            <div class="main">
                                <strong class="subject">
                                    <a href="/users/${user.id}">${user.username}</a>
                                </strong>
                                <c:if test="${user.deleted}">
                                    <strong class="subject" style="color:red">
                                        탈퇴한 사용자
                                    </strong>
                                </c:if>
                                <div class="auth-info">
                                    <i class="icon-add-comment"></i>
                                    <span class="time">${user.createdAt}</span>
                                </div>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/include/footer.jsp" %>
