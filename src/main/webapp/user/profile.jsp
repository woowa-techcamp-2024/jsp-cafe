<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../header.jsp" %>
<%@ include file="../navbar.jsp" %>


<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading"><h4>Profiles</h4></div>
            <div class="panel-body">
                <div class="well well-sm">
                    <div class="media">
                        <a class="thumbnail pull-left" href="#">
                            <img class="media-object" src="<c:url value='/images/80-text.png'/>" alt="프로필 이미지">
                        </a>
                        <div class="media-body">
                            <h4 class="media-heading">${user.nickname}</h4>
                            <p>
                                <a href="#" class="btn btn-xs btn-default">
                                    <span class="glyphicon glyphicon-envelope"></span>&nbsp;${user.email}
                                </a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../footer.jsp" %>