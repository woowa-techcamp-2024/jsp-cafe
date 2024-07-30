<%@ page import="codesquad.javacafe.member.dto.response.MemberResponseDto" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="kr">

<jsp:include page="/common/header.jsp" />
<body>
<jsp:include page="/common/topbar.jsp" />
<jsp:include page="/common/navbar.jsp" />

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading"><h4>Profiles</h4></div>
            <div class="panel-body">
                <div class="well well-sm">
                    <div class="media">
                        <a class="thumbnail pull-left" href="#">
                            <img class="media-object" src="../../images/80-text.png">
                        </a>
                        <%
                            var member = (MemberResponseDto)request.getAttribute("memberInfo");
                        %>
                        <div class="media-body">
                            <h4 class="media-heading"><%=member.getName()%></h4>
                            <p>
                                <span class="glyphicon glyphicon-envelope"></span>ID : <%=member.getUserId()%>
                            </p>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<script src="../../js/jquery-2.2.0.min.js"></script>
<script src="../../js/bootstrap.min.js"></script>
<script src="../../js/scripts.js"></script>
	</body>
</html>