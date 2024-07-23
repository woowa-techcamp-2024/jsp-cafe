<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="woowa.camp.jspcafe.service.dto.UserResponse" %>

<%@ include file="../header/header.jsp" %>

<div class="container" id="main">
    <div class="col-md-10 col-md-offset-1">
        <div class="panel panel-default">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>사용자 아이디</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<UserResponse> users = (List<UserResponse>)request.getAttribute("users");
                    if(users != null) {
                        for(int i = 0; i < users.size(); i++) {
                            UserResponse user = users.get(i);
                %>
                <tr>
                    <th scope="row"><%= user.id() %></th>
                    <td><a href="/users/<%= user.id() %>" class="btn btn-link"><%= user.userId() %></a></td>
                    <td><%= user.name() %></td>
                    <td><%= user.email() %></td>
                    <td><a href="#" class="btn btn-success" role="button">수정</a></td>
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

<%@ include file="../footer/footer.jsp" %>