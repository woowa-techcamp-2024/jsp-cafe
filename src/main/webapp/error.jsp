<%@ page contentType="text/html; charset=UTF-8" language="java" isErrorPage="true" %>
<%
    // 예외 타입을 확인하고 상태 코드를 설정
    if (exception instanceof exception.UnAuthorizedException) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드
    } else if(exception instanceof exception.TomcatException) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 500 상태 코드 (기본값)
    }
%>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="/header.jsp" %>
<body>
<%@ include file="/navigationbar.jsp" %>
<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title">오류가 발생했습니다.</h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-doc">
                        <div class="error-details">
                            <h2>Error Details:</h2>
<%--                            <p>Exception Type: <%= exception.getClass().getName() %></p>--%>
                            <p>Message: <%= exception.getMessage() %></p>
                        </div>
                    </div>
                </article>
            </div>
        </div>
    </div>
</div>
<%@ include file="/footer.jsp" %>
</body>
</html>