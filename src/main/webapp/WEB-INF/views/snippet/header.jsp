<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="navbar navbar-default" id="subnav">
    <div class="col-md-12">
        <div class="navbar-header">
            <a href="#" style="margin-left:15px;" class="navbar-btn btn btn-default btn-plus dropdown-toggle"
               data-toggle="dropdown"><i class="glyphicon glyphicon-home" style="color:#dd1111;"></i> Home <small><i
                    class="glyphicon glyphicon-chevron-down"></i></small></a>
            <ul class="nav dropdown-menu">
                <li><a href="user/profile.jsp"><i class="glyphicon glyphicon-user" style="color:#1111dd;"></i>
                    Profile</a>
                </li>
                <li class="nav-divider"></li>
                <li><a href="#"><i class="glyphicon glyphicon-cog" style="color:#dd1111;"></i> Settings</a></li>
            </ul>

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
                    <c:when test="${sessionScope.signInUser == null}">
                        <li><a href="/users/sign-in" role="button">로그인</a></li>
                        <li><a href="/users/signup" role="button">회원가입</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="/users/${signInUser.id}" role="button">${signInUser.name}</a></li>
                        <li>
                            <form name="logoutForm" method="post"
                                  action="${pageContext.request.contextPath}/users/sign-out" id="logoutForm"></form>
                            <a href="#" role="button"
                               onclick="document.getElementById('logoutForm').submit(); return false;">로그아웃</a>
                        </li>
                        <li><a href="/users/edit/${signInUser.id}" role="button">개인정보수정</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</div>
