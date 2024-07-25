<%@ page import="codesquad.javacafe.member.dto.response.MemberResponseDto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="../../css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="../../css/styles.css" rel="stylesheet">
</head>
<body>
<%@include file="../../common/topbar.jsp"%>
<%@include file="../../common/navbar.jsp"%>

<div class="container" id="main">
   <div class="col-md-10 col-md-offset-1">
      <div class="panel panel-default">
          <table class="table table-hover">
              <thead>
                <tr>
                    <th>#</th> <th>사용자 아이디</th> <th>이름</th> </th>
                </tr>
              </thead>
              <tbody>
                <%
                    var members = (List<MemberResponseDto>) request.getAttribute("memberList");
                    if (Objects.nonNull(members)) {
                        int row = 1;
                        for(MemberResponseDto memberResponseDto : members) {
                %>
                    <tr>
                        <th scope="row"><%= row++%></th> <td><a href = "/api/users/profile?userId=<%=memberResponseDto.getUserId()%>"><%=memberResponseDto.getUserId()%></a></td><td><%=memberResponseDto.getName()%></td><td><a href="#" class="btn btn-success" role="button">수정</a></td>
                    </tr>
              <%
                    }
                  }
              %>
              </tbody>
          </table>
        </div>
    </div>
</div>

<!-- script references -->
<script src="../../js/jquery-2.2.0.min.js"></script>
<script src="../../js/bootstrap.min.js"></script>
<script src="../../js/scripts.js"></script>
	</body>
</html>