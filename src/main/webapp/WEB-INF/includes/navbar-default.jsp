<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="navbar navbar-default" id="subnav">
    <div class="col-md-12">
        <div class="navbar-header">
            <a href="#" style="margin-left:15px;" class="navbar-btn btn btn-default btn-plus dropdown-toggle" data-toggle="dropdown"><i class="glyphicon glyphicon-home" style="color:#dd1111;"></i> Home <small><i class="glyphicon glyphicon-chevron-down"></i></small></a>
            <ul class="nav dropdown-menu">
                <li><a href="../user/profile.html"><i class="glyphicon glyphicon-user" style="color:#1111dd;"></i> Profile</a></li>
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
                <%
                    String userId = (String) session.getAttribute("userId");
                    if (userId == null) {
                %>
                <li class="active"><a href="../index.jsp">Posts</a></li>
                <li><a href="/users/login" role="button">로그인</a></li>
                <li><a href="/userPage?action=register" role="button">회원가입</a></li>
                <%
                } else {
                %>
                <li><a href="/users/logout" role="button">로그아웃</a></li>
                <li><a href="/users/update/<%=(long)session.getAttribute("userSeq")%>" role="button">개인정보수정</a></li>
                <%
                    }
                %>
            </ul>
        </div>
    </div>
</div>