<%@ page import="domain.User" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="../header.jsp" %>
<body>
<%@ include file="../navigationbar.jsp" %>
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
                      for (int i = 0; i < users.size(); i++){
                          User user = users.get(i);
                  %>
                  <tr>
                    <th scope="row"><%=i + 1%></th>
                      <td> <c:out value="<%= user.getUserId() %>"/></td>
                      <td>
                          <span onclick="location.href='<%= request.getContextPath() %>/users/<%= user.getId() %>'" style="cursor:pointer;">
                              <c:out value="<%= user.getName() %>" />
                          </span>
                      </td>
                      <td><c:out value="<%=user.getEmail()%>" /></td>
                      <td><a href="/users/<%=user.getId()%>/form" class="btn btn-success" role="button">수정</a></td>
                  </tr>
                  <% } %>
              </tbody>
          </table>
        </div>
    </div>
</div>

<%@ include file="../footer.jsp" %>
	</body>
</html>