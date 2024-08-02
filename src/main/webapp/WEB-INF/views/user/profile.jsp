<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page import="woowa.camp.jspcafe.repository.dto.response.UserResponse" %>
<jsp:include page="../common/header.jsp" />

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading"><h4>Profiles</h4></div>
            <div class="panel-body">
                <div class="well well-sm">
                    <div class="media">
                        <a class="thumbnail pull-left" href="#">
                            <img class="media-object" src="${pageContext.request.contextPath}/static/images/80-text.png">
                        </a>
                        <div class="media-body">
                            <%
                                UserResponse user = (UserResponse) request.getAttribute("user");
                                if (user != null) {
                            %>
                            <h4 class="media-heading"><%= user.getNickname() %></h4>
                            <p>
                                <a href="#" class="btn btn-xs btn-default">
                                    <span class="glyphicon glyphicon-envelope"></span>&nbsp;<%= user.getEmail() %>
                                </a>
                            </p>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>

</body>
</html>