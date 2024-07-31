<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.example.domain.User" %>
<%@ page import="org.example.constance.SessionName" %>
<header>
    <a href="/">
        <h1>HELLO, WEB!</h1>
    </a>
    <div class="buttons">
        <% if (session.getAttribute(SessionName.USER.getName()) != null) {
            User user = (User)session.getAttribute(SessionName.USER.getName());
        %>
            <div class="user-info">
                <span> <%= user.getNickname() %> 님 </span>
            </div>
            <a href="/api/logout"><button class="btn">로그아웃</button></a>
            <a href="/users/update-form/<%= user.getUserId() %>"><button class="btn">내 정보 수정하기</button></a>
        <%
            } else {
        %>
            <a href="/login"><button class="btn">로그인</button></a>
            <a href="/users/register"><button class="btn">회원가입</button></a>
        <%
           }
        %>
        <a href="/"><button class="btn">글 목록</button></a>
        <a href="/users"><button class="btn">사용자 목록</button></a>
    </div>
</header>