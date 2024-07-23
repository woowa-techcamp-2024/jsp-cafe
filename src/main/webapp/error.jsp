<%--
  Created by IntelliJ IDEA.
  User: woowatech08
  Date: 2024. 7. 23.
  Time: 오후 6:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<%@include file="/share/header.jsp" %>
<body>
<%@include file="/share/navbar.jsp" %>
<%@include file="/share/sub_navbar.jsp" %>
<div class="container" id="main">
    <div class="row justify-content-center">
        <div class="col-md-6 col-md-offset-3">
            <div class="panel panel-default content-main text-center p-4">
                <div class="error-code mb-3" style="font-size: 120px; font-weight: bold;">
                    ${statusCode}
                </div>
                <div class="error-message mb-4" style="padding-top: 24px; font-size: 24px;">
                    ${errorMessage}
                </div>
                <div style="padding-top: 48px; font-size: 24px;">
                    <button class="btn btn-primary" style="max-width: 300px; width: 100%; margin: 0 auto;" onclick="history.back()">
                        뒤로가기
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="js/jquery-2.2.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/scripts.js"></script>
</body>
</html>
