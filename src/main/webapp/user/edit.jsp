<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../header.jsp" %>
<%@ include file="../navbar.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <form id="userUpdateForm">
                <div class="form-group">
                    <label for="userId">사용자 아이디</label>
                    <input class="form-control" id="userId" name="userId" value="${user.userId}" readonly/>
                </div>
                <div class="form-group">
                    <label for="password">비밀 번호 확인</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="비밀번호"/>
                </div>
                <div class="form-group">
                    <label for="nickname">이름</label>
                    <input type="text" class="form-control" id="nickname" name="nickname" value="${user.nickname}" placeholder="이름"/>
                </div>
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email" class="form-control" id="email" name="email" value="${user.email}" placeholder="이메일"/>
                </div>
                <button type="button" onclick="updateUser()" class="btn btn-success clearfix pull-right">수정하기</button>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script>
    // updateUser 함수
    function updateUser() {
        const userId = document.getElementById('userId').value;
        const nickname = document.getElementById('nickname').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        axios.put(`/users/${user.id}`, null, {
            params: {
                userId: userId,
                nickname: nickname,
                email: email,
                password: password
            }
        })
            .then(function (response) {
                console.log('Response:', response);
                if (response.status === 200) {
                    // 200번대 응답에서 Location 헤더를 확인하여 리다이렉트
                    if (response.headers['location']) {
                        window.location.href = response.headers['location'];
                    } else {
                        console.log('Location header not found in response.');
                    }
                } else {
                    console.log('Unexpected status code:', response.status);
                }
            })
            .catch(function (error) {
                console.error('Error updating user:', error);
                alert('사용자 정보 업데이트 중 오류가 발생했습니다.');
                // 에러 처리 로직 추가
            });
    }
</script>

<%@ include file="../footer.jsp" %>
