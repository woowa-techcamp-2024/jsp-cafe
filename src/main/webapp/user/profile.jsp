<%@ page import="org.example.demo.domain.User" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<jsp:include page="/components/header.jsp"/>
<body>
<jsp:include page="/components/nav.jsp"/>
<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading"><h4>Profiles</h4></div>
            <div class="panel-body">
                <div class="well well-sm">
                    <div class="media">
                        <a class="thumbnail pull-left" href="#">
                            <img class="media-object" src="/static/images/80-text.png">
                        </a>
                        <%
                            User user = (User) request.getAttribute("user");
                        %>
                        <div class="media-body">
                            <h4 class="media-heading"><%= user.getName()%>
                            </h4>
                            <p>
                                <a href="#" class="btn btn-xs btn-default"><span
                                        class="glyphicon glyphicon-envelope"></span>&nbsp <%= user.getEmail()%>
                                </a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<jsp:include page="/components/footer.jsp"/>

</body>
</html>