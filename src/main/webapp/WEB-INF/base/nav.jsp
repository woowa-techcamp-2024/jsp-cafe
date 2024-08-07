<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="navbar navbar-default" id="subnav">
    <div class="col-md-12">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse2">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse2">
            <ul class="nav navbar-nav navbar-right">
                <li class="active"><a href="/">Posts</a></li>
                <c:choose>
                    <c:when test="${sessionScope.userId == null}">
                        <li><a href="/login" role="button">로그인</a></li>
                        <li><a href="/user/regist" role="button">회원가입</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="/logout" role="button">로그아웃</a></li>
                        <li><a href="/users/${sessionScope.userId}/form" role="button">개인정보수정</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</div>