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
                <c:forEach var="contentResponse" items="${requestScope.questionList}">
                <li>
                    <div class="wrap">
                        <div class="main">
                            <strong class="subject">
                                <a href="/questions/${contentResponse.id}"><c:out
                                        value="${contentResponse.title}"/>
                                </a>
                            </strong>
                            <div class="auth-info">
                                <i class="icon-add-comment"></i>
                                <span class="time">${contentResponse.createdAt}</span>
                                <a href="/users/${contentResponse.writer}"
                                   class="author"><c:out value="${contentResponse.writer}"/>
                                </a>

                            </div>
                            <div class="reply" title="댓글">
                                <i class="icon-reply"></i>
                                <span class="point">🔍</span>
                            </div>
                        </div>
                    </div>
                </li>
                </c:forEach>
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
                        <a href="${pageContext.request.contextPath}/qna/form.html"
                           class="btn btn-primary pull-right"
                           role="button">질문하기</a>
                    </div>
                </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/component/scripts.jsp"/>
</body>
</html>

