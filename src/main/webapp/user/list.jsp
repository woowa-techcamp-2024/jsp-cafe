<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.example.domain.User" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User List</title>
    <link rel="stylesheet" href="/css/common.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
<%
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
%>
<div class="container">
    <%@ include file="/common/header.jsp" %>
    <div class="info-box">
        <h2>멤버리스트</h2>
        <p>참여자를 확인할 수 있습니다.</p>
    </div>
    <main>
        <table id="userTable">
            <thead>
            <tr>
                <th>닉네임</th>
                <th>이메일</th>
                <th>회원가입일</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
        <button id="loadMoreUsers" class="btn" style="display: none;">더보기</button>
    </main>
</div>
<script>
    $(document).ready(function() {
       var allUsers = [];
       var currentPage = 1;
       var usersPerPage = 10;

       loadUsers();

       function loadUsers() {
           $.ajax({
               url: '/api/users',
               type: 'GET',
               dataType: 'json',
               success: function(users) {
                   allUsers = users;
                   updateUserList();
               },
               error: function(xhr, status, error) {
                   console.error("Error fetching users:", error);
               }
           });
       }

       function updateUserList() {
           var tbody = $('#userTable tbody');
           tbody.empty();

           var startIndex = 0;
           var endIndex = currentPage * usersPerPage;

           for (var i = startIndex; i < endIndex && i < allUsers.length; i++) {
               var user = allUsers[i];
               var row = $('<tr class="clickable-tr"></tr>');
               row.append($('<td></td>').text(user.nickname));
               row.append($('<td></td>').text(user.email));
               row.append($('<td></td>').text(user.createdDt));
               row.click(function() {
                   goToUserDetail(user.userId);
               });
               tbody.append(row);
           }

           updateLoadMoreButton();
       }

       function updateLoadMoreButton() {
           var $loadMoreButton = $('#loadMoreUsers');
           if (currentPage * usersPerPage < allUsers.length) {
               $loadMoreButton.show();
           } else {
               $loadMoreButton.hide();
           }
       }

       $('#loadMoreUsers').click(function() {
           currentPage++;
           updateUserList();
       });
   });

    function goToUserDetail(userId) {
        window.location.href = '/users/' + userId;
    }
</script>
</body>
</html>
