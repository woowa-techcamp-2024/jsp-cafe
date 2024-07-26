<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="cafe.domain.entity.User" %>
<%@ page import="java.util.Map" %>
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
                    <th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th><th></th>
                </tr>
              </thead>
              <tbody>
                <%
                    Map<String, User> users = (Map<String, User>) request.getAttribute("users");
                    int i = 0;
                    for (String key : users.keySet()) {
                        i++;
                        User user = users.get(key);
                %>
                <tr>
                    <th scope="row"><%=i%></th>
                    <td><%=user.getUserid()%></td>
                    <td><%=user.getName()%></td>
                    <td><%=user.getEmail()%></td>
                    <td><a href="/users/<%=key%>" class="btn btn-success" role="button">조회</a></td>
                    <td><a href="/users/<%=key%>/edit" class="btn btn-success" role="button">수정</a></td>
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
<%@ include file="/WEB-INF/components/script.jsp" %>
	</body>
</html>