<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="woowa.camp.jspcafe.service.dto.UserResponse" %>

<%@ include file="../common/header.jsp" %>

<div class="container" id="main">
    <div class="col-md-10 col-md-offset-1">
        <div class="panel panel-default">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>닉네임</th>
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
                    <td><a href="/users/<%= user.id() %>" class="btn btn-link"><%= user.nickname() %></a></td>
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

<%@ include file="../common/footer.jsp" %>