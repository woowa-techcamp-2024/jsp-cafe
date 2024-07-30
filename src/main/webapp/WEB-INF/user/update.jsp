<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" language="java" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <title>유저 수정 페이지</title>
    <%@include file="/WEB-INF/includes/head.jsp"%>
</head>
<body>

<%@include file="/WEB-INF/includes/navbar-fixed-top.jsp"%>
<%@include file="/WEB-INF/includes/navbar-default.jsp"%>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <form name="question" id="user-update-form">
                <div class="form-group">
                    <label for="userId">아이디</label>
                    <input class="form-control" readonly id="userId" name="userId" placeholder="User ID" value="${user.userId}">
                </div>
                <div class="form-group">
                    <label for="password">현재 비밀번호</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                </div>
                <div class="form-group">
                    <label for="new-password">새로운 비밀번호</label>
                    <input type="password" class="form-control" id="new-password" name="new-password" placeholder="Password">
                </div>
                <div class="form-group">
                    <label for="name">이름</label>
                    <input class="form-control" id="name" name="name" placeholder="Name">
                </div>
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="Email">
                </div>
                <button type="button" class="btn btn-success clearfix pull-right" onclick="updateUser()">수정</button>
                <div class="clearfix" />
            </form>
        </div>
    </div>
</div>

<!-- script references -->
<%@include file="/WEB-INF/includes/script-references.jsp"%>
</body>
</html>
<script>
    function updateUser() {
        const form = document.querySelector('#user-update-form');
        const url = "/users";
        const formData = new FormData(form);
        const data = {};

        formData.forEach((value, key) => {
            data[key] = value;
        });

        fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then(response => {
            if (response.ok) {
                window.location.href = '/userPage?action=detail&seq=${user.userSeq}';
            } else {
                alert('수정 중 오류가 발생했습니다');
            }
        }).catch(error => {
            alert('수정 중 오류가 발생했습니다: ' + error.message);
        });
    }
</script>
