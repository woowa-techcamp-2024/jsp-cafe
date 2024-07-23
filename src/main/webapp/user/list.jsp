<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="cafe.domain.entity.User" %>
<%@ page import="java.util.List" %>
<%@ include file="/WEB-INF/components/head.jsp" %>
<body>
<%@ include file="/WEB-INF/components/header.jsp"%>
<%@ include file="/WEB-INF/components/navigation.jsp"%>

<div class="container" id="main">
   <div class="col-md-10 col-md-offset-1">
      <div class="panel panel-default">
          <table class="table table-hover">
              <thead>
                <tr>
                    <th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th>
                </tr>
              </thead>
              <tbody>
                <%
                    List<User> users = (List<User>) request.getAttribute("users");
                    for (int i = 0; i < users.size(); i++) {
                %>
                <tr>
                    <th scope="row"><%=i+1%></th>
                    <td><%=users.get(i).getId()%></td>
                    <td><%=users.get(i).getName()%></td>
                    <td><%=users.get(i).getEmail()%></td>
                    <td><a href="/users/<%=users.get(i).getId()%>" class="btn btn-success" role="button">조회</a></td>
                </tr>
                <%
                    }
                %>
              </tbody>
          </table>
        </div>
    </div>
</div>

<!-- script references -->
<script src="../js/jquery-2.2.0.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/scripts.js"></script>
	</body>
</html>