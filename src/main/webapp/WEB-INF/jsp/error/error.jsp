<%@ page import="camp.woowa.jspcafe.exception.CustomException" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ include file="../template/header.jsp"%>

<%
    CustomException e = (CustomException) request.getAttribute("camp.woowa.jspcafe.exception.CustomException");
    Integer statusCode = (e != null) ? e.getStatusCode() : 500;
    String message = (e != null) ? e.getMessage() : "알 수 없는 오류가 발생했습니다.";
    String description = (e != null) ? e.getDescription() : "관리자에게 문의하세요.";
%>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <div class="error-page">
                <h2>오류가 발생했습니다</h2>
                <div class="form-group">
                    <label>상태 코드</label>
                    <input class="form-control" type="text" value="<%= statusCode %>" readonly/>
                </div>
                <div class="form-group">
                    <label>오류 메시지</label>
                    <input type="text" class="form-control" value="<%= message %>" readonly/>
                </div>
                <div class="form-group">
                    <label>상세 설명</label>
                    <textarea rows="5" class="form-control" readonly><%= description %></textarea>
                </div>
                <button type="button" class="btn btn-primary clearfix pull-right" onclick="history.back()">이전 페이지로</button>
                <div class="clearfix" />
            </div>
        </div>
    </div>
</div>

<%@ include file="../template/footer.jsp"%>
