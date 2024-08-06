<%--
  Created by IntelliJ IDEA.
  User: KyungMin Lee
  Date: 24. 7. 23.
  Time: 오후 5:08
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE>
<html lang="ko">
<jsp:include page="/WEB-INF/jsp/component/headers.jsp"/>
<body>
<jsp:include page="/WEB-INF/jsp/component/navbar.jsp"/>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">
                <div class="row">
                    <div class="col-md-3"></div>
                    <div class="col-md-6 text-center">
                        <ul class="pagination center-block" style="display:inline-block;">
                            <li><a href="#">«</a></li>
                            <li><a href="#">1</a></li>
                            <li><a href="#">2</a></li>
                            <li><a href="#">3</a></li>
                            <li><a href="#">4</a></li>
                            <li><a href="#">5</a></li>
                            <li><a href="#">»</a></li>
                        </ul>
                    </div>
                    <div class="col-md-3 qna-write">
                        <a href="${pageContext.request.contextPath}/questions"
                           class="btn btn-primary pull-right"
                           role="button">질문하기</a>
                    </div>
                </div>
            </ul>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/component/scripts.jsp"/>
</body>
</html>
