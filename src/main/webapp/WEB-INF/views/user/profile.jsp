<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="kr">
<jsp:include page="../snippet/meta_header.jsp"/>
<body>
<jsp:include page="../snippet/navigation.jsp"/>
<jsp:include page="../snippet/header.jsp"/>

<c:set var="currentUser" value="${requestScope.user}"/>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading"><h4>Profiles</h4></div>
            <div class="panel-body">
                <div class="well well-sm">
                    <div class="media">
                        <a class="thumbnail pull-left" href="#">
                            <img class="media-object" src="../../../images/80-text.png">
                        </a>
                        <div class="media-body">
                            <h4 class="media-heading">${currentUser.name}</h4>
                            <p>
                                <a href="#" class="btn btn-xs btn-default"><span
                                        class="glyphicon glyphicon-envelope"></span>${currentUser.email}</a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<jsp:include page="../snippet/script.jsp"/>
</body>
</html>
